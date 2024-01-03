package com.sec.instagramclone.ui.common.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = context?.let { newIntent<T>(it) }
    intent?.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent!!, requestCode, options)
    } else {
        startActivityForResult(intent!!, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent {
    val intent = Intent(context, T::class.java)
    if (context.getActivity() == null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return intent
}

fun FragmentActivity.addOnBackPressedCallback(
    lifecycleOwner: LifecycleOwner = this, handleBackPress: () -> Unit = {}
): OnBackPressedCallback {
    val callback = object :
        OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPress.invoke()
        }
    }
    onBackPressedDispatcher.addCallback(lifecycleOwner, callback)
    return callback
}

fun Activity.getRootView(): ViewGroup {
    return findViewById(android.R.id.content)
}