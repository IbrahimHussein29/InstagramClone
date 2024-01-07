package com.sec.instagramclone.ui.main.profile.profileReels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentProfileReelsBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileReelsFragment : Fragment() {

    private var _binding: FragmentProfileReelsBinding? = null
    private val viewModel by viewModels<ProfileReelsVM>()
    private val adapter by lazy {
        ProfileReelsAdapter(arrayListOf())
    }
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileReelsBinding.inflate(layoutInflater)
        bindData()
        return binding.root
    }

    private fun bindData() {
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        updateUserReelsToProfile()
    }

    private fun updateUserReelsToProfile() {

        viewModel.addReelToProfile(ReelBody())
        collectData()

    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.reelData) {
            it?.onSuccess {
                binding.progressBar.visibility=View.GONE
                adapter.reelList = it
                adapter.notifyDataSetChanged()
            }

        }
    }

}

