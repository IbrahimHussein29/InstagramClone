package com.sec.instagramclone.ui.main.profile.profilePosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentProfilePostsBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePostsFragment : Fragment() {
    private var _binding: FragmentProfilePostsBinding? = null
    private val viewModel by viewModels<ProfilePostsVM>()
    private val adapter by lazy {
        ProfilePostsAdapter(arrayListOf())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePostsBinding.inflate(layoutInflater)


        bindData()

        return binding.root
    }

    private fun bindData() {
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        updateUserPostsToProfile()
    }

    private fun updateUserPostsToProfile() {


viewModel.addPostToProfile(PostBody())
        collectData()

    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.post) {
            it?.onSuccess {
                binding.progressBar.visibility=View.GONE
                adapter.postList=it
                adapter.notifyDataSetChanged()
            }

        }
    }

}

