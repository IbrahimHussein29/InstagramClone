package com.sec.instagramclone.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.databinding.FragmentHomeBinding
import com.sec.instagramclone.ui.main.upload.AddPostFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setFragmentListeners()
        return binding.root
    }

    private fun setFragmentListeners() {
        // address
        setFragmentResultListener(AddPostFragment.RESULT_ADD_POST) { _, _ ->
            openAddDialog()
        }
    }

    private fun openAddDialog() {
        findNavController().navigate(R.id.addFragment)
    }
}