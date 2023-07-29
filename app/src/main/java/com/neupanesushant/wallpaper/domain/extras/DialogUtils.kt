package com.neupanesushant.wallpaper.domain.extras

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.neupanesushant.wallpaper.R

object DialogUtils {

    fun neutralAlertDialog(context: Context, titleMessage: String, description: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(titleMessage)
            .setMessage(description)
            .setNeutralButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}