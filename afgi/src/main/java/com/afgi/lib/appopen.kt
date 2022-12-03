package com.afgi.lib

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*

private var appOpenAd: AppOpenAd? = null
private var loadTime: Long = 0

private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
    val dateDifference = Date().time - loadTime
    val numMilliSecondsPerHour: Long = 3600000
    return dateDifference < numMilliSecondsPerHour * 4
}

fun Context.requestAppOpen(placement: String, callBack: (str: String) -> Unit) {
    AppOpenAd.load(
        this,
        placement,
        AdRequest.Builder().build(),
        AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
        object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                super.onAdLoaded(ad)
                appOpenAd = ad
                loadTime = Date().time
                callBack.invoke(LOADED_AD)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                callBack.invoke(p0.toString())
            }
        }
    )
}

fun isLoadedAppOpen(): Boolean {
    return appOpenAd != null
}


fun Activity.showAppOpen(callBack: () -> Unit) {
    if (appOpenAd != null) {
        appOpenAd?.show(this)
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                callBack.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                callBack.invoke()
            }

            override fun onAdShowedFullScreenContent() {

            }
        }
    } else callBack.invoke()
}