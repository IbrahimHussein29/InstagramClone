package com.sec.instagramclone.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sec.instagramclone.R
import com.sec.instagramclone.data.Constants
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onError
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentUpdateProfileBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.isValidEmail
import com.sec.instagramclone.ui.common.extensions.setImageUrl
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    private var _binding:FragmentUpdateProfileBinding? = null
    private val viewModel by viewModels<ProfileVM>()
    private lateinit var user: UserBody
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
        _binding = FragmentUpdateProfileBinding.inflate(layoutInflater)
        viewModel.getUserData()

        getProfileData()
        setClickListeners()
        return binding.root
    }

    private fun getProfileData() {
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user = it
                binding.nameEdtTxt.setText(user.name)
                binding.emailEdtTxt.setText(user.email)
                binding.emailEdtTxt.isEnabled=false
                binding.passwordEdtTxt.setText(user.password)
                imageUri= user.userImage
                if (!user.userImage.isNullOrEmpty()) {
                    binding.profileImg.setImageUrl(user.userImage)
                }

            }
        }
    }

    private fun setClickListeners() {
        binding.btnUpdateProfile.setOnSafeClickListener {

            updateUser()
        }
        binding.addImage.setOnSafeClickListener {
            launcher.launch("image/*")

        }
    }


    private fun updateUser() {

        val user = UserBody(
            name = binding.nameEdtTxt.text.toString(),
            email = binding.emailEdtTxt.text.toString(),
            password = binding.passwordEdtTxt.text.toString(),
          userImage = imageUri



        )
        if (validateUser(user)) {
            viewModel.updateUserData(user)

            collectLatestLifecycleFlow(viewModel.userData) {

                it?.onSuccess {

                    findNavController().navigate(R.id.profileFragment)

                }
                it?.onError { _, _ ->
                    binding.emailEdtTxt.error =
                        resources.getString(R.string.email_address_already_exists)
                }
            }
        }
    }

    private fun validateUser(user: UserBody): Boolean {
        var valid = true
        if (user.name.isEmpty()) {
            binding.nameEdtTxt.error = resources.getString(R.string.missing_data)
            valid = false
        }

        if (user.email.isEmpty() && user.email.isValidEmail().not()) {
            binding.emailEdtTxt.error = resources.getString(R.string.invalid_email_address)
            valid = false
        }
        if (user.password.isEmpty() && user.password.length < 8) {
            binding.passwordEdtTxt.error = resources.getString(R.string.invalid_password_weak)
            valid = false
        }


        return valid
    }


}