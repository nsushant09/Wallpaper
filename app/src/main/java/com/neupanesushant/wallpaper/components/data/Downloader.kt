package com.neupanesushant.wallpaper.components.data

interface Downloader {

    fun downloadFile(url : String, mimeType : String) : Long
}