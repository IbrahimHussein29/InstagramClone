package com.sec.instagramclone.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sec.instagramclone.R
import com.sec.instagramclone.databinding.FragmentInvalidEmailPasswordDialogBinding
import com.sec.instagramclone.ui.base.BaseDialogFragment
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener


class InvalidEmailPasswordDialogFragment : BaseDialogFragment() {
private var _binding:FragmentInvalidEmailPasswordDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentInvalidEmailPasswordDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
binding.btnOk.setOnSafeClickListener{
    findNavController().popBackStack()
}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}