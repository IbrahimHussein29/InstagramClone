package com.sec.instagramclone.ui.main.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.Constants
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentUploadPostBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.setImageCenteredCropped
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPostFragment : Fragment() {
    private var _binding: FragmentUploadPostBinding? = null
    private val viewModel by viewModels<UploadVM>()
    private lateinit var user: UserBody
    private val binding get() = _binding!!
    private var imageUri: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.progressBar.visibility = View.VISIBLE
        uri?.let {
            viewModel.uploadImage(uri, Constants.POST_FOLDER) {
                binding.postImg.setImageCenteredCropped(it)
                if (it != null) {
                    imageUri = it
                    binding.progressBar.visibility = View.GONE
                }
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadPostBinding.inflate(layoutInflater)
        setToolbar()
        bindData()
        return binding.root
    }

    private fun bindData() {
        binding.postImg.setOnSafeClickListener {
            launcher.launch("image/*")

        }
        binding.postBtn.setOnSafeClickListener {
            postImage()
        }

        binding.cancelPostBtn.setOnSafeClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun postImage() {
        if (imageUri != null) {
            getPostData()
            collectData()
        } else return

    }

    private fun getPostData() {
        viewModel.getUserData()
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user = it
                val post = PostBody(
                    user.name,
                    user.userImage.toString(),
                    System.currentTimeMillis(),
                    imageUri!!,
                    binding.captionEdtTxt.text.toString()
                )
                val media = MediaBody(
                    user.name,
                    user.userImage,
                    System.currentTimeMillis(),
                    imageUri!!,
                    "",
                    binding.captionEdtTxt.text.toString()

                )

                viewModel.postImage(post, media)

            }
        }
    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.postImage) {
            it?.onSuccess {
                findNavController().navigate(R.id.homeFragment)
            }

        }
    }

    private fun setToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(binding.materialToolbar)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            openAddDialog(RESULT_ADD_POST)
        }

    }

    private fun openAddDialog(result: String, bundle: Bundle = bundleOf()) {
        findNavController().popBackStack()
        setFragmentResult(result, bundle)
    }


    companion object {
        const val RESULT_ADD_POST = "result_add_post"

    }


}