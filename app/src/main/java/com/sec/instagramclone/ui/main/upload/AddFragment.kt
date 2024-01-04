package com.sec.instagramclone.ui.main.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sec.instagramclone.R
import com.sec.instagramclone.databinding.FragmentAddBinding
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener


class AddFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

      _binding=FragmentAddBinding.inflate(layoutInflater)

        bindData()
        return binding.root
    }

    private fun bindData() {
        binding.uploadPost.setOnSafeClickListener{
            findNavController().navigate(R.id.addPostFragment)

        }
        binding.uploadReels.setOnSafeClickListener{
            findNavController().navigate(R.id.addReelsFragment)
        }
    }


}