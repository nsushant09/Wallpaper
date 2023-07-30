package com.neupanesushant.wallpaper.domain.usecase.ad

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class BannerAdsManager(private val context: Context) {


    private val adRequest = AdRequest.Builder().build()


    init {
        initialize()
    }

    private fun initialize() {
        MobileAds.initialize(context)
    }

    fun loadAd(view: AdView) {
        view.loadAd(adRequest)
    }
}