package com.neupanesushant.wallpaper.components.extras

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.neupanesushant.wallpaper.showText

class DownloadCompletedReciever : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if(id != -1L && context != null){
                context.showText("Download Completed")
            }
        }

        if(intent?.action == "android.intent.action.WALLPAPER_CHANGED"){
            context?.showText("Wallpaper Changed")
        }
    }
}