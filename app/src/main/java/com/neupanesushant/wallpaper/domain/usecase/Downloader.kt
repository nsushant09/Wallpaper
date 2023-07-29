package com.neupanesushant.wallpaper.domain.usecase

interface Downloader {

    fun downloadFile(url : String, mimeType : String) : Long
}