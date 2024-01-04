package com.sec.instagramclone.ui.main.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.databinding.FragmentAddPostBinding
import com.sec.instagramclone.ui.main.MainActivity
import com.sec.instagramclone.ui.main.profile.ProfileVM


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPostBinding.inflate(layoutInflater)
        setToolbar()

        return binding.root
    }

    private fun setToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(binding.materialToolbar)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
           openAddDialog(RESULT_ADD_POST)
        }

    }

    private fun openAddDialog(result: String, bundle: Bundle= bundleOf()) {
        findNavController().popBackStack()
        setFragmentResult(result, bundle)
    }


    companion object {
        const val RESULT_ADD_POST = "result_add_post"

    }


}