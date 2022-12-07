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
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener

private var mInterstitialAd = arrayOfNulls<InterstitialAd?>(2)
private var interstitialAdFacebook: com.facebook.ads.InterstitialAd? = null
private var applovineInterstitialAd = arrayOfNulls<MaxInterstitialAd?>(3)

var mCanShowAd = false

var intertitialad: InMobiInterstitial? = null

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

fun Context.requestInMobi(placement: String, isExitAds: Boolean, listener: (str: String) -> Unit) {
    val intertitialeventlistner: InterstitialAdEventListener =
        object : InterstitialAdEventListener() {
            override fun onAdLoadSucceeded(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdLoadSucceeded(p0, p1)
                mCanShowAd = true

            }

            override fun onAdLoadFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
                super.onAdLoadFailed(p0, p1)
            }

            override fun onAdDisplayed(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdDisplayed(p0, p1)
            }

            override fun onAdDismissed(p0: InMobiInterstitial) {
                super.onAdDismissed(p0)
            }
        }


    intertitialad = InMobiInterstitial(this, placement.toLong(), intertitialeventlistner)

    intertitialad?.load()
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

fun Activity.requestSplashApplovin(id: String, callBack: (status: String) -> Unit) {
    if (applovineInterstitialAd[0] == null) {
        applovineInterstitialAd[0] = MaxInterstitialAd(id, this)
        applovineInterstitialAd[0]?.setListener(
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
        applovineInterstitialAd[0]?.loadAd()
    }

}

fun Activity.requestApplovine(id: String, isExitAds: Boolean, callBack: (status: String) -> Unit) {
    if ((if (isExitAds) applovineInterstitialAd[2] == null else applovineInterstitialAd[1] == null)) {
        if (isExitAds) applovineInterstitialAd[2] = MaxInterstitialAd(id, this)
        else applovineInterstitialAd[1] = MaxInterstitialAd(id, this)
        if (isExitAds) applovineInterstitialAd[2]?.setListener(
            object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd?) {

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {
                    applovineInterstitialAd[2] = null
                }

                override fun onAdClicked(ad: MaxAd?) {

                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    applovineInterstitialAd[2] = null
                    callBack.invoke("error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}")
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                }
            })
        else applovineInterstitialAd[1]?.setListener(
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

        if (isExitAds) applovineInterstitialAd[2]?.loadAd() else applovineInterstitialAd[1]?.loadAd()
    }

}

fun Activity.showExit(listener: () -> Unit) {
    if (applovineInterstitialAd[2] != null && applovineInterstitialAd[2]?.isReady == true) {
        applovineInterstitialAd[2]?.showAd()
        applovineInterstitialAd[2]?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {

            }

            override fun onAdDisplayed(ad: MaxAd?) {

            }

            override fun onAdHidden(ad: MaxAd?) {
                applovineInterstitialAd[2] = null
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

fun isExitLoaded(): Boolean {
    return mInterstitialAd[1] != null || (applovineInterstitialAd[2] != null && applovineInterstitialAd[2]?.isReady == true)
}

fun isLoaded(): Boolean {
    return mInterstitialAd[0] != null || intertitialad != null || IronSource.isInterstitialReady() || (applovineInterstitialAd[1] != null && applovineInterstitialAd[1]?.isReady == true)
}

fun isSplashLoaded(): Boolean {
    return mInterstitialAd[0] != null || IronSource.isInterstitialReady() || (applovineInterstitialAd[0] != null && applovineInterstitialAd[0]?.isReady == true)
}

fun isLoadedFacebook(): Boolean {
    return interstitialAdFacebook != null && interstitialAdFacebook?.isAdLoaded == true
}

fun Activity.show(placementKey: String, listener: () -> Unit) {
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

    } else if (mInterstitialAd[0] != null) {
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
    } else if (intertitialad != null) {
        if (mCanShowAd) intertitialad?.show()
        intertitialad?.setListener(object : InterstitialAdEventListener() {
            override fun onAdLoadSucceeded(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdLoadSucceeded(p0, p1)

            }

            override fun onAdLoadFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
                super.onAdLoadFailed(p0, p1)
                intertitialad = null
                listener.invoke()
            }

            override fun onAdDisplayed(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdDisplayed(p0, p1)
            }

            override fun onAdDismissed(p0: InMobiInterstitial) {
                super.onAdDismissed(p0)
                intertitialad = null
                listener.invoke()
            }
        })
    } else {
        listener.invoke()
    }
}

fun Activity.showSplash(placementKey: String, listener: () -> Unit) {
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

    } else if (mInterstitialAd[0] != null) {
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

fun showFacebook() {
    interstitialAdFacebook?.show()
}



