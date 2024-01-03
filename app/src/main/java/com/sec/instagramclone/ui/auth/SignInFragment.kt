package com.sec.instagramclone.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.LoginBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentSignInBinding
import com.sec.instagramclone.ui.MainActivity
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.isValidEmail
import com.sec.instagramclone.ui.common.extensions.launchActivity
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val viewModel by viewModels<LoginVM>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater)

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

        val loginBody = LoginBody(
            email = binding.emailEdtTxt.text.toString(),
            password = binding.passwordEdtTxt.text.toString()

            )



        if (validateLogin(loginBody)) {

            viewModel.login(loginBody)
    collectData()
}


    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.loginData) {
            it?.onSuccess {

                launchActivity<MainActivity> { }
                requireActivity().finish()
            }
        }

    }

    private fun validateLogin(loginBody: LoginBody): Boolean {
        var valid = true
        if (!loginBody.email.isValidEmail()) {
            binding.emailEdtTxt.error = resources.getString(R.string.invalid_email_address)
            valid = false
        }
        if (loginBody.password.length < 8) {
            binding.passwordEdtTxt.error = resources.getString(R.string.invalid_password_weak)
            valid = false
        }
        return valid

    }


}