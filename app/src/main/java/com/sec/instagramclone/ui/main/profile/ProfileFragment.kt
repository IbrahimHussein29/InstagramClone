package com.sec.instagramclone.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentProfileBinding
import com.sec.instagramclone.ui.auth.LoginVM
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var user: UserBody
    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<LoginVM>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(layoutInflater)
        viewModel.getUserData()
        collectUserProfileData()
        bindData()
findNavController().popBackStack(R.id.updateProfileFragment, true)
        return binding.root
    }

    private fun bindData() {
        binding.editProfileBtn.setOnSafeClickListener{

            findNavController().navigate(R.id.updateProfileFragment)
        }
    }

    private fun collectUserProfileData() {
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user=it
                binding.name.text=user.name
                binding.bio.text=user.email
                if(!user.userImage.isNullOrEmpty()){
                    Picasso.get().load(user.userImage).into(binding.profileImg)
                }

            }
        }

    }





}