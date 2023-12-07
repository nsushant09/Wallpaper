package com.neupanesushant.wallpaper.domain.extras

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.neupanesushant.wallpaper.BuildConfig
import java.util.Locale

//class ConsentInformationHelper(private val activity: Activity, private val context: Context) {
//
//    private val consentInformation: ConsentInformation =
//        UserMessagingPlatform.getConsentInformation(context)
//
//    private var consentDebugSettings = ConsentDebugSettings
//        .Builder(context)
//        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//        .addTestDeviceHashedId(getDeviceId())
//        .build()
//
//    private var params = getParams()
//
//    fun setupConsentInformation(onConsentFormDismissed: (ConsentInformation) -> Unit) {
//        consentInformation.requestConsentInfoUpdate(activity, params,
//            {
//                loadAndShowConsentForm(onConsentFormDismissed)
//            },
//            { requestConsentError ->
//                Log.w(
//                    "TAG", String.format(
//                        "%s: %s",
//                        requestConsentError.errorCode,
//                        requestConsentError.message
//                    )
//                )
//            })
//    }
//
//    private fun loadAndShowConsentForm(onConsentFormDismissed: (ConsentInformation) -> Unit) {
//        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
//            activity
//        ) { loadAndShowError ->
//            // Consent gathering failed.
//            Log.w(
//                "TAG", String.format(
//                    "%s: %s",
//                    loadAndShowError?.errorCode,
//                    loadAndShowError?.message
//                )
//            )
//            onConsentFormDismissed(consentInformation)
//        }
//    }
//
//    private fun getParams(): ConsentRequestParameters {
//        val builder = ConsentRequestParameters
//            .Builder()
//            .setTagForUnderAgeOfConsent(false)
//
//        if (BuildConfig.DEBUG) {
//            builder.setConsentDebugSettings(consentDebugSettings)
//        }
//
//        return builder.build()
//    }
//
//    @SuppressLint("HardwareIds")
//    private fun getDeviceId(): String {
//        val androidID: String =
//            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
//
//        return MD5.encrypt(androidID).toUpperCase(Locale.ROOT)
//    }
//
//    fun setParams(params: ConsentRequestParameters) {
//        this.params = params;
//    }
//
//    fun canRequestAds(): Boolean = consentInformation.canRequestAds()
//}