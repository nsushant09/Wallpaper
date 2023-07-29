package com.neupanesushant.wallpaper.domain.usecase.ad

import android.app.Activity
import com.google.android.gms.ads.AdRequest

interface AdsManager {
    val adRequest: AdRequest

    fun initialize()
    fun loadAd()
    fun showAd(activity: Activity)
    fun <T> setAd(ad: T)

    fun <T> setContentCallback(contentCallback: T)
}