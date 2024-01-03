package com.sec.instagramclone.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.Constants
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentSignUpBinding
import com.sec.instagramclone.ui.MainActivity
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.isValidEmail
import com.sec.instagramclone.ui.common.extensions.launchActivity
import com.sec.instagramclone.ui.common.extensions.setImageUrl
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val viewModel by viewModels<LoginVM>()
    private val binding get() = _binding!!
    private var imageUri: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.uploadImage(uri, Constants.USER_PROFILE_FOLDER) {
                binding.profileImg.setImageUrl(it)
                if (it != null) {
                    imageUri = it
                }
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)

        setClickListeners()
        return binding.root
    }

    private fun  setClickListeners() {
        binding.btnRegister.setOnSafeClickListener {
            register()
        }
        binding.addImage.setOnSafeClickListener {
            launcher.launch("image/*")

        }
        binding.textLogin.setOnSafeClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

        }


    }



    private fun register() {
        val userBody = UserBody(
            name = binding.nameEdtTxt.text.toString(),
            email = binding.emailEdtTxt.text.toString(),
            password = binding.passwordEdtTxt.text.toString(),
            userImage = imageUri
        )

        if (validateRegister(userBody)) {
            viewModel.register(userBody)

            collectData()
        }
    }
    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.registerData) {
            it?.onSuccess {
                launchActivity<MainActivity> { }
                requireActivity().finish()
            }
        }

    }

    private fun validateRegister(userBody: UserBody): Boolean {
        var valid = true
        if (userBody.name.isEmpty()) {
            binding.nameEdtTxt.error = resources.getString(R.string.missing_data)
            valid = false
        }

        if (userBody.email.isEmpty() && userBody.email.isValidEmail().not()) {
            binding.emailEdtTxt.error = resources.getString(R.string.invalid_email_address)
            valid = false
        }
        if (userBody.password.isEmpty() && userBody.password.length < 8) {
            binding.passwordEdtTxt.error = resources.getString(R.string.invalid_password_weak)
            valid = false
        }


        return valid
    }


}