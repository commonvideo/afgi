package com.afgi.lib

import android.app.Activity
import android.content.Context
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*

private var appOpenAd: AppOpenAd? = null
private var applovineAppOpenAd: MaxAppOpenAd? = null
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

fun Context.requestApplovinAppOpen(placement: String, callBack: (status: String) -> Unit) {
    applovineAppOpenAd = MaxAppOpenAd(placement, this)
    applovineAppOpenAd?.setListener(object : MaxAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            loadTime = Date().time
            callBack.invoke(LOADED_AD)
        }

        override fun onAdDisplayed(ad: MaxAd?) {

        }

        override fun onAdHidden(ad: MaxAd?) {
            applovineAppOpenAd = null
        }

        override fun onAdClicked(ad: MaxAd?) {

        }

        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            applovineAppOpenAd = null
            callBack.invoke("error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}")

        }

        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

        }
    })

    applovineAppOpenAd?.loadAd()
}


fun isLoadedAppOpen(): Boolean {
    return ((appOpenAd != null) || (applovineAppOpenAd != null && applovineAppOpenAd?.isReady == true)) && wasLoadTimeLessThanNHoursAgo()
}


fun Activity.showAppOpen(callBack: () -> Unit) {
    if (applovineAppOpenAd != null && applovineAppOpenAd?.isReady == true) {
        applovineAppOpenAd?.showAd()
        applovineAppOpenAd?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                applovineAppOpenAd = null
                callBack.invoke()
            }

            override fun onAdClicked(ad: MaxAd?) {

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                applovineAppOpenAd = null
                callBack.invoke()
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                applovineAppOpenAd = null
                callBack.invoke()
            }
        })
    } else if (appOpenAd != null) {
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