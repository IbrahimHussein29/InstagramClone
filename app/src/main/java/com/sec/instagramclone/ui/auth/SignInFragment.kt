package com.sec.instagramclone.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.common.onError
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentSignInBinding
import com.sec.instagramclone.ui.main.MainActivity
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.isValidEmail
import com.sec.instagramclone.ui.common.extensions.launchActivity
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val viewModel by viewModels<LoginVM>()
    private var email = ""
    private var password = ""
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater)
        viewModel.getUserData()
        setClickListeners()
        return binding.root
    }


    private fun setClickListeners() {
        binding.btnLogin.setOnSafeClickListener {
            login()
        }
        binding.btnRegister.setOnSafeClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun login() {


        email = binding.emailEdtTxt.text.toString()
        password = binding.passwordEdtTxt.text.toString()


        if (validateLogin(email, password)) {

            viewModel.login(email, password)
            collectData()
        }


    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.user) {
            it?.onSuccess {

                launchActivity<MainActivity> { }
                requireActivity().finish()
            }
            it?.onError { _, _ ->
                findNavController().navigate(R.id.invalidEmailPasswordDialogFragment)
            }
        }

    }

    private fun validateLogin(email: String, password: String): Boolean {
        var valid = true
        if (!email.isValidEmail()) {
            binding.emailEdtTxt.error = resources.getString(R.string.invalid_email_address)
            valid = false
        }
        if (password.length < 8) {
            binding.passwordEdtTxt.error = resources.getString(R.string.invalid_password_weak)
            valid = false
        }
        return valid

    }


}