package com.sec.instagramclone.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sec.instagramclone.data.common.onError
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.ActivityLauncherBinding
import com.sec.instagramclone.ui.main.MainActivity
import com.sec.instagramclone.ui.auth.AuthActivity
import com.sec.instagramclone.ui.auth.LoginVM
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private val viewModel by viewModels<LoginVM>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.loggedUser()
        collectData()

    }

    private fun collectData() {
        collectLatestLifecycleFlow(viewModel.user) {
            it?.onSuccess {
                Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }, 2000
        )

    }


            it?.onError { _, _ ->
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }, 2000
                )
            }
        }
    }




}