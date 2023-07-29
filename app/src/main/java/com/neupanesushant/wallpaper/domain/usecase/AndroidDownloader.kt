package com.neupanesushant.wallpaper.domain.usecase

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(
    context : Context
) : Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java) as DownloadManager

    override fun downloadFile(url: String, mimeType : String): Long {

        val currentTime = System.currentTimeMillis()
        val fileName = currentTime.toString() + getExtenstionType(mimeType)
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(mimeType)
            .setTitle(fileName)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        return downloadManager.enqueue(request)
    }

    private fun getExtenstionType(mimeType: String) : String{
        var counter = 0;
        for(char in mimeType.toCharArray()){
            counter ++
            if(char == '/'){
                break;
            }
        }

        var output = "."
        for(i in counter until mimeType.toCharArray().size){
            output += mimeType.toCharArray().get(i)
        }
        return output
    }

    companion object{
        const val IMAGE_JPEG = "image/jpeg"
        const val IMAGE_PNG = "image/png"
        const val IMAGE_GIF = "image/gif"
        const val IMAGE_JPG = "image/jpg"
        const val IMAGE_SVG = "image/svg+xml"
        const val VIDEO_MP4 = "video/mp4"
        const val AUDIO_MP4 = "audio/mp4"
    }
}