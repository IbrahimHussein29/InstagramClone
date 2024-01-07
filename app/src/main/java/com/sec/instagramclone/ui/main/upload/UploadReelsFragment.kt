package com.sec.instagramclone.ui.main.upload

import android.app.ProgressDialog
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
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentUploadReelsBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadReelsFragment : Fragment() {

    private var _binding: FragmentUploadReelsBinding? = null
    private lateinit var user: UserBody
    private val viewModel by viewModels<UploadVM>()
    private lateinit var progressDialog: ProgressDialog
    private val binding get() = _binding!!
    private var videoUri: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        progressDialog.show()

        uri?.let {
            viewModel.uploadReel(uri, Constants.REEL_FOLDER) {
                if (it != null) {
                    videoUri = it
                    closeProgressDialog()
                }
            }
        }

    }

    private fun closeProgressDialog() {

        progressDialog.dismiss()
        binding.addReelBtn.text = resources.getText(R.string.reel_uploaded_successfully)
        binding.addReelBtn.setTextColor(resources.getColor(R.color.pink))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadReelsBinding.inflate(layoutInflater)
        setToolbar()
        bindData()
        return binding.root
    }

    private fun setToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(binding.materialToolbar)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            openAddDialog(RESULT_ADD_REEL)
        }

    }

    private fun bindData() {
        binding.addReelBtn.setOnSafeClickListener {
            progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle(resources.getText(R.string.uploading__))


            launcher.launch("video/*")

        }

        binding.postBtn.setOnSafeClickListener {
            postReel()
        }

        binding.cancelPostBtn.setOnSafeClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }


    private fun postReel() {


        if (videoUri != null) {
            getReelData()
            collectData()
        } else return

    }

    private fun getReelData() {
        viewModel.getUserData()
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user = it
                val reel = ReelBody(
                    videoUri!!,
                    binding.captionEdtTxt.text.toString(),
                    user.userImage
                )
                viewModel.postReel(reel)

            }
        }
    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.postReel) {
            it?.onSuccess {

                findNavController().navigate(R.id.homeFragment)
            }

        }
    }

    private fun openAddDialog(result: String, bundle: Bundle = bundleOf()) {
        findNavController().popBackStack()
        setFragmentResult(result, bundle)
    }

    companion object {
        const val RESULT_ADD_REEL = "result_add_reel"

    }

}