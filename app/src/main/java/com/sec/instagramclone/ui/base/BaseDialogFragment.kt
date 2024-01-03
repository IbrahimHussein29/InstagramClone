package com.sec.instagramclone.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sec.instagramclone.R
import com.sec.instagramclone.ui.common.extensions.getScreenWidth

abstract class BaseDialogFragment : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogConstraints()
    }

    private fun setDialogConstraints() {
        val width = getScreenWidth() - resources.getDimensionPixelSize(R.dimen.dialog_width_space)
        dialog?.window?.setLayout(
            width,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


    }
}