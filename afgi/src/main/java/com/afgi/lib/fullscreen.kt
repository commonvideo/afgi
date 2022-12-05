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
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener

private var mInterstitialAd = arrayOfNulls<InterstitialAd?>(2)
private var interstitialAdFacebook: com.facebook.ads.InterstitialAd? = null
private var applovineInterstitialAd = arrayOfNulls<MaxInterstitialAd?>(2)

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
    return mInterstitialAd[1] != null || (applovineInterstitialAd[1] != null && applovineInterstitialAd[1]?.isReady == true)
}

fun isLoaded(): Boolean {
    return mInterstitialAd[0] != null || IronSource.isInterstitialReady() || (applovineInterstitialAd[0] != null && applovineInterstitialAd[0]?.isReady == true)
}


fun Activity.showExit(listener: () -> Unit) {
    if (applovineInterstitialAd[1] != null && applovineInterstitialAd[1]?.isReady == true) {
        applovineInterstitialAd[1]?.showAd()
        applovineInterstitialAd[1]?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                applovineInterstitialAd[1] = null
                listener.invoke()
            }

            override fun onAdClicked(ad: MaxAd?) {

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {

            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

            }
        })
    } else if (mInterstitialAd[1] != null) {
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
    if (applovineInterstitialAd[0] != null && applovineInterstitialAd[0]?.isReady == true) {
        applovineInterstitialAd[0]?.showAd()
        applovineInterstitialAd[0]?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                applovineInterstitialAd[0] = null
                listener.invoke()
            }

            override fun onAdClicked(ad: MaxAd?) {

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {

            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

            }
        })
    } else if (IronSource.isInterstitialReady()) {
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

    } else if ( mInterstitialAd[0]!=null) {
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

fun Activity.requestApplovine(id: String, isExitAds: Boolean, callBack: (status: String) -> Unit) {
    if ((if (isExitAds) applovineInterstitialAd[1] == null else applovineInterstitialAd[0] == null)) {
        if (isExitAds) applovineInterstitialAd[1] = MaxInterstitialAd(id, this)
        else applovineInterstitialAd[0] = MaxInterstitialAd(id, this)
        if (isExitAds) applovineInterstitialAd[1]?.setListener(
            object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd?) {

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {
                    applovineInterstitialAd[1] = null
                }

                override fun onAdClicked(ad: MaxAd?) {

                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    applovineInterstitialAd[1] = null
                    callBack.invoke("error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}")
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                }
            })
        else applovineInterstitialAd[0]?.setListener(
            object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd?) {

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {
                    applovineInterstitialAd[0] = null
                }

                override fun onAdClicked(ad: MaxAd?) {

                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    applovineInterstitialAd[0] = null
                    callBack.invoke("error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}")
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                }
            })

        if (isExitAds) applovineInterstitialAd[1]?.loadAd() else applovineInterstitialAd[0]?.loadAd()
    }

}




