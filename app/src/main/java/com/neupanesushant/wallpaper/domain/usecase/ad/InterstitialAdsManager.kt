package com.neupanesushant.wallpaper.domain.usecase.ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdsManager(
    private val context: Context,
    private val adLoadCallback: InterstitialAdLoadCallback
) {

    private var interstitialAd: InterstitialAd? = null

    private val adRequest: AdRequest get() = AdRequest.Builder().build()

    init {
        initialize()
    }

    fun initialize() {
        MobileAds.initialize(context)
    }

    fun loadAd(adCode : String) {
        InterstitialAd.load(
            context,
            adCode,
            adRequest,
            adLoadCallback
        )
    }

    fun showAd(activity: Activity) {
        interstitialAd?.show(activity)
    }

    fun setAd(ad: InterstitialAd) {
        interstitialAd = ad
    }

    fun setContentCallback(contentCallback: FullScreenContentCallback) {
        interstitialAd?.fullScreenContentCallback = contentCallback as FullScreenContentCallback
    }
}