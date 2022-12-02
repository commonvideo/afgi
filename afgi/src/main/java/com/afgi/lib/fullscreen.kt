package com.afgi.lib

import android.app.Activity
import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


private var mInterstitialAd = arrayOfNulls<InterstitialAd?>(2)
private var applovineInterstitialAd = arrayOfNulls<MaxInterstitialAd?>(2)
private var interstitialAdFacebook: com.facebook.ads.InterstitialAd? = null

fun Context.request(placement: String, isExitAds: Boolean, listener: FullCallBack) {
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
                listener.onError(adError.toString())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                if (isExitAds)
                    mInterstitialAd[1] = interstitialAd
                else
                    mInterstitialAd[0] = interstitialAd
            }
        })
}


fun Context.requestFacebook(placement: String, listener: FullCallBack) {
    interstitialAdFacebook = com.facebook.ads.InterstitialAd(this, placement)
    interstitialAdFacebook?.loadAd(
        interstitialAdFacebook?.buildLoadAdConfig()
            ?.withAdListener(object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
                    listener.onError("errorCode=${p1?.errorCode} errorMessage=${p1?.errorMessage}")
                }

                override fun onAdLoaded(p0: Ad?) {
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


fun Activity.requestApplovine(id: String, isExitAds: Boolean, listener: FullCallBack) {
    if ((if (isExitAds) applovineInterstitialAd[1] else applovineInterstitialAd[0]) == null) {
        if (isExitAds) applovineInterstitialAd[1] else applovineInterstitialAd[0] =
            MaxInterstitialAd(id, this)
        if (isExitAds) applovineInterstitialAd[1] else applovineInterstitialAd[0]?.setListener(
            object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd?) {

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {

                }

                override fun onAdClicked(ad: MaxAd?) {

                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    if (isExitAds) applovineInterstitialAd[1] =
                        null else applovineInterstitialAd[0] = null
                    listener.onError("error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}")
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                }
            })

        if (isExitAds) applovineInterstitialAd[1] else applovineInterstitialAd[0]?.loadAd()
    }

}

fun requestIronSource(listener: FullCallBack) {
    /*if (!IronSource.isInterstitialReady()) {
        IronSource.loadInterstitial()
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                Log.e("requestIronSource ", "onInterstitialAdReady")
            }

            override fun onInterstitialAdLoadFailed(ironSourceError: IronSourceError) {
                Log.e("requestIronSource ", "onInterstitialAdLoadFailed")
                listener.onError(ironSourceError.toString())
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

    }*/
}


fun isExitLoaded(): Boolean {
    return mInterstitialAd[1] != null || (applovineInterstitialAd[1] != null && applovineInterstitialAd[1]?.isReady == true)
}

fun isLoaded(): Boolean {
    return mInterstitialAd[0] != null /*|| IronSource.isInterstitialReady()*/ || (applovineInterstitialAd[0] != null && applovineInterstitialAd[0]?.isReady == true)
}


fun Activity.showExit(listener: CallBack) {
    if (applovineInterstitialAd[1] != null && applovineInterstitialAd[1]?.isReady == true) {
        applovineInterstitialAd[1]?.showAd()
        applovineInterstitialAd[1]?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                listener.onCompleted()
            }

            override fun onAdClicked(ad: MaxAd?) {

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {

            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

            }
        })
    } else if (isLoaded()) {
        mInterstitialAd[1]?.show(this)
        mInterstitialAd[1]?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd[1] = null
                listener.onCompleted()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                mInterstitialAd[1] = null
                listener.onCompleted()
            }
        }
    } else {
        listener.onCompleted()
    }
}

fun Activity.show(placementKey: String, listener: CallBack) {
    if (applovineInterstitialAd[0] != null && applovineInterstitialAd[0]?.isReady == true) {
        applovineInterstitialAd[0]?.showAd()
        applovineInterstitialAd[0]?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                listener.onCompleted()
            }

            override fun onAdClicked(ad: MaxAd?) {

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {

            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

            }
        })
    } /*else if (IronSource.isInterstitialReady()) {
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
                listener.onCompleted()
            }

            override fun onInterstitialAdShowSucceeded() {

            }

            override fun onInterstitialAdShowFailed(ironSourceError: IronSourceError) {

            }

            override fun onInterstitialAdClicked() {

            }
        })

    }*/ else if (isLoaded()) {
        mInterstitialAd[0]?.show(this)
        mInterstitialAd[0]?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd[0] = null
                listener.onCompleted()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                mInterstitialAd[0] = null
                listener.onCompleted()
            }
        }
    } else {
        listener.onCompleted()
    }
}

fun isLoadedFacebook(): Boolean {
    return interstitialAdFacebook != null && interstitialAdFacebook?.isAdLoaded == true
}


fun showFacebook() {
    interstitialAdFacebook?.show()
}

