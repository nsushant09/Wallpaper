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
) : AdsManager {

    private var interstitialAd: InterstitialAd? = null
    override val adRequest: AdRequest get() = AdRequest.Builder().build()

    init {
        initialize()
    }

    override fun initialize() {
        MobileAds.initialize(context)
    }

    override fun loadAd() {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            adLoadCallback
        )
    }

    override fun showAd(activity: Activity) {
        interstitialAd?.show(activity)
    }

    override fun <T> setAd(ad: T) {
        interstitialAd = ad as InterstitialAd
    }

    override fun <T> setContentCallback(contentCallback: T) {
        interstitialAd?.fullScreenContentCallback = contentCallback as FullScreenContentCallback
    }
}