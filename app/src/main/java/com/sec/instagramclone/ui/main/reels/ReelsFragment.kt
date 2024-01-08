package com.sec.instagramclone.ui.main.reels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentReelsBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReelsFragment : Fragment() {
    private var _binding: FragmentReelsBinding? = null
   private val viewModel by viewModels<ReelsVM>()
    private val adapter by lazy {
       ReelsAdapter(arrayListOf())
    }
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding= FragmentReelsBinding.inflate(layoutInflater)
bindData()
        return binding.root
    }

    private fun bindData() {
    binding.viewPager.adapter=adapter
        viewModel.getReels(ReelBody())
        collectData()

    }


    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.reelData) {
            it?.onSuccess {
                adapter.reelList = it
                adapter.notifyDataSetChanged()
            }

        }
    }

}