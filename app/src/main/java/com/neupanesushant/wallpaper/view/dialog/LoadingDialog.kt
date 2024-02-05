package com.neupanesushant.wallpaper.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.neupanesushant.wallpaper.R
import com.neupanesushant.wallpaper.databinding.DialogLoadingBinding

class LoadingDialog(
    private val infoText: String = "Loading..."
) : DialogFragment() {
    private lateinit var binding: DialogLoadingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_loading,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dialogInfoText.text = infoText
    }

}