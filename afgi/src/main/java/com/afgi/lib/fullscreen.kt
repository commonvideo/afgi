package com.afgi.lib

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener

private var mInterstitialAd = arrayOfNulls<InterstitialAd?>(2)
private var interstitialAdFacebook: com.facebook.ads.InterstitialAd? = null

fun Context.request(placement: String, isExitAds: Boolean, listener: (str: String) -> Unit) {
    InterstitialAd.load(
        this,
        placement,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (isExitAds)
                    mInterstitialAd[1] = null
                else
                    mInterstitialAd[0] = null
                listener.invoke(adError.toString())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                if (isExitAds)
                    mInterstitialAd[1] = interstitialAd
                else
                    mInterstitialAd[0] = interstitialAd
                listener.invoke(LOADED_AD)
            }
        })
}


fun Context.requestFacebook(placement: String, listener: (str: String) -> Unit) {
    interstitialAdFacebook = com.facebook.ads.InterstitialAd(this, placement)
    interstitialAdFacebook?.loadAd(
        interstitialAdFacebook?.buildLoadAdConfig()
            ?.withAdListener(object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                    listener.invoke("errorCode=${p1?.errorCode} errorMessage=${p1?.errorMessage}")
                }

                override fun onAdLoaded(p0: Ad?) {
                    listener.invoke(LOADED_AD)
                }

                override fun onAdClicked(p0: Ad?) {
                }

                override fun onLoggingImpression(p0: Ad?) {
                }

                override fun onInterstitialDisplayed(p0: Ad?) {
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                }
            })
            ?.build()
    )
}


fun requestIronSource(listener: (str: String) -> Unit) {
    if (!IronSource.isInterstitialReady()) {
        IronSource.loadInterstitial()
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                listener.invoke(LOADED_AD)
                Log.e("requestIronSource ", "onInterstitialAdReady")
            }

            override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {
                Log.e("requestIronSource ", "onInterstitialAdLoadFailed")
                listener.invoke(ironSourceError.toString())
            }

            override fun onInterstitialAdOpened() {
            }

            override fun onInterstitialAdClosed() {

            }

            override fun onInterstitialAdShowSucceeded() {

            }

            override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {

            }

            override fun onInterstitialAdClicked() {

            }
        })

    }
}


fun isExitLoaded(): Boolean {
    return mInterstitialAd[1] != null
}

fun isLoaded(): Boolean {
    return mInterstitialAd[0] != null || IronSource.isInterstitialReady()
}


fun Activity.showExit(listener: () -> Unit) {
    if (isLoaded()) {
        mInterstitialAd[1]?.show(this)
        mInterstitialAd[1]?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd[1] = null
                listener.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                mInterstitialAd[1] = null
                listener.invoke()
            }
        }
    } else {
        listener.invoke()
    }
}

fun Activity.show(placementKey: String, listener: () -> Unit) {
    if (IronSource.isInterstitialReady()) {
        if (placementKey.isNotEmpty())
            IronSource.showInterstitial(placementKey)
        else
            IronSource.showInterstitial()
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
            }

            override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {

            }

            override fun onInterstitialAdOpened() {
            }

            override fun onInterstitialAdClosed() {
                listener.invoke()
            }

            override fun onInterstitialAdShowSucceeded() {

            }

            override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {

            }

            override fun onInterstitialAdClicked() {

            }
        })

    } else if (isLoaded()) {
        mInterstitialAd[0]?.show(this)
        mInterstitialAd[0]?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd[0] = null
                listener.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                mInterstitialAd[0] = null
                listener.invoke()
            }
        }
    } else {
        listener.invoke()
    }
}

fun isLoadedFacebook(): Boolean {
    return interstitialAdFacebook != null && interstitialAdFacebook?.isAdLoaded == true
}


fun showFacebook() {
    interstitialAdFacebook?.show()
}

