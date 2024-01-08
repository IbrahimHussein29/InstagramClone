package com.sec.instagramclone.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentSearchBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.main.reels.ReelsAdapter
import com.sec.instagramclone.ui.main.reels.ReelsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding:FragmentSearchBinding?= null
    private val viewModel by viewModels<SearchVM>()
    var flag=true
    private val adapter by lazy {
        SearchAdapter(arrayListOf())
    }

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSearchBinding.inflate(layoutInflater)
bindData()
        return binding.root
    }

    private fun bindData() {
        binding.recyclerView.adapter=adapter
        viewModel.getAllUsers(UserBody())
        collectData()
        binding.searchView.doAfterTextChanged {
            val name=binding.searchView.text.toString()
            if(name.isNotEmpty()){
                viewModel.searchUsers(name, UserBody())
                collectSearchData()
            }else{
                binding.recyclerView.adapter=adapter
                viewModel.getAllUsers(UserBody())
                collectData()

            }

        }


    }

    private fun collectSearchData() {
        collectLatestLifecycleFlow(viewModel.searchData) {
            it?.onSuccess {
                adapter.userList = it
                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.userData) {
            it?.onSuccess {
                adapter.userList = it
                adapter.notifyDataSetChanged()
            }

        }
    }


}