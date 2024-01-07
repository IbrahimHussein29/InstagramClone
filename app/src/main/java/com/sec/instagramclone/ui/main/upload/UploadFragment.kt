package com.sec.instagramclone.ui.main.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sec.instagramclone.R
import com.sec.instagramclone.databinding.FragmentUploadBinding
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

      _binding=FragmentUploadBinding.inflate(layoutInflater)

        bindData()
        return binding.root
    }

    private fun bindData() {
        binding.uploadPost.setOnSafeClickListener{
            findNavController().navigate(R.id.uploadPostFragment)

        }
        binding.uploadReels.setOnSafeClickListener{
            findNavController().navigate(R.id.uploadReelsFragment)
        }
    }


}