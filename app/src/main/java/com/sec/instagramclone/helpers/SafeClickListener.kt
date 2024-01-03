package com.sec.instagramclone.helpers

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < DEFAULT_CLICK_INTERVAL) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }

    companion object {
        const val DEFAULT_CLICK_INTERVAL = 700
    }
}
