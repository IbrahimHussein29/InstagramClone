package com.sec.instagramclone.ui.common.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sec.instagramclone.helpers.SafeClickListener

fun View?.setOnSafeClickListener(onSafeClick: View.OnClickListener?) {
    if (this == null) return

    if (onSafeClick == null) {
        setOnClickListener(onSafeClick)
        return
    }

    val safeClickListener = SafeClickListener {
        onSafeClick.onClick(it)
    }

    setOnClickListener(safeClickListener)
}

fun ImageView.setImageUrl(url: String?, placeHolder: Int = -1) {
    Glide.with(this).load(url).placeholder(placeHolder).thumbnail(0.05f)
        .into(this)
}