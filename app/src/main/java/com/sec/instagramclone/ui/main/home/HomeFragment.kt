package com.sec.instagramclone.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentHomeBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.main.upload.UploadPostFragment
import com.sec.instagramclone.ui.main.upload.UploadReelsFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var user: UserBody
    private lateinit var playerList:ArrayList<ExoPlayer>
    private val viewModel by viewModels<HomeVM>()
    private var exoPlayer:ExoPlayer?=null
    var playBackPosition= 0L
    var playWhenReady=true
    private val adapter by lazy {
        HomeAdapter(arrayListOf(), arrayListOf())

    }



    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setToolbar()
        bindData()

        setFragmentListeners()
    }


    private fun bindData() {
        viewModel.getUserData()
        collectUserProfileData()
        binding.homeRecyclerView.adapter = adapter

        viewModel.getAllMedia(MediaBody())

        collectPostData()
    }



    private fun collectUserProfileData() {
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user=it
                if(!user.userImage.isNullOrEmpty()){
                    Picasso.get().load(user.userImage).into(binding.profileImg)
                }

            }
        }

    }


    private fun collectPostData() {
        collectLatestLifecycleFlow(viewModel.mediaData) {
            it?.onSuccess {
                adapter.items =it
               playerList=ArrayList<ExoPlayer>()
                    for(item in adapter.items){
                        exoPlayer=ExoPlayer.Builder(requireContext()).build()
                        var mediaItem=MediaItem.fromUri(item.videoUrl)
                        exoPlayer?.addMediaItem(mediaItem)
                        exoPlayer?.seekTo(playBackPosition)
                        exoPlayer?.playWhenReady=playWhenReady
                        exoPlayer?.prepare()

                        playerList.add(exoPlayer!!)

                    }
                adapter.players=playerList

                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

    private fun setToolbar() {

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.homeToolbar)



    }

    private fun setFragmentListeners() {
        // address
        setFragmentResultListener(UploadPostFragment.RESULT_ADD_POST) { _, _ ->
            openAddDialog()
        }
        setFragmentResultListener(UploadReelsFragment.RESULT_ADD_REEL) { _, _ ->
            openAddDialog()
        }
    }

    private fun openAddDialog() {
        findNavController().navigate(R.id.uploadFragment)
    }
    private fun releasePlayer(){
        for(exoPlayer in playerList){
            exoPlayer.let { it->
                playBackPosition=it.currentPosition
                playWhenReady=it.playWhenReady
               it.release()



            }
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    
    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }



}