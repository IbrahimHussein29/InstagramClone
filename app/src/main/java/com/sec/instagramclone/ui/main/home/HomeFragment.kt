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
    private val viewModel by viewModels<HomeVM>()
    private val adapter by lazy {
        HomeAdapter(arrayListOf()) { item ->
            handleItemClicked(item)

        }
    }


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        setToolbar()
        bindData()

        setFragmentListeners()
        return binding.root
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

    private fun handleItemClicked(item: MediaBody) {
//        when (item) {
//            is PostBody -> {
//
//            }
//        }
    }
}