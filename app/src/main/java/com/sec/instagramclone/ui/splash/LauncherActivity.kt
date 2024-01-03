package com.sec.instagramclone.ui.splash

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sec.instagramclone.R
import com.sec.instagramclone.ui.auth.AuthActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)


        redirectToNextScreen()
    }

    private fun redirectToNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }, 3000
        )

    }
}