package com.afgi.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize.BANNER_HEIGHT_50
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdView


fun Activity.requestBanner(
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    val adView = AdView(this)
    adView.setAdSize(getAdSize())
    adView.adUnitId = placement
    adView.loadAd(
        AdRequest.Builder()
            .build()
    )
    adView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            val layout = LinearLayout(this@requestBanner)
            layout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            layout.orientation = LinearLayout.VERTICAL
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val heightPx = LinearLayout.LayoutParams.WRAP_CONTENT
            adView.layoutParams = LinearLayout.LayoutParams(width, heightPx)
            layout.addView(adView)
            listener.invoke(layout, LOADED_AD)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            listener.invoke(null, loadAdError.toString())
        }
    }

}

fun Activity.getAdSize(): AdSize {
    // Determine the screen width (less decorations) to use for the ad width.
    val display = windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val widthPixels = outMetrics.widthPixels.toFloat()
    val density = outMetrics.density
    val adWidth = (widthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}


fun Activity.requestFacebookBanner(
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    val layout = LinearLayout(this@requestFacebookBanner)
    layout.layoutParams =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    layout.orientation = LinearLayout.VERTICAL
    val width = LinearLayout.LayoutParams.MATCH_PARENT
    val heightPx = LinearLayout.LayoutParams.WRAP_CONTENT
    val adView =
        com.facebook.ads.AdView(this, placement, BANNER_HEIGHT_50)
    layout.addView(adView)
    adView.loadAd(adView.buildLoadAdConfig().withAdListener(object : AdListener(),
        com.facebook.ads.AdListener {
        override fun onError(p0: Ad?, p1: AdError?) {
            listener.invoke(null, "errorCode= ${p1?.errorCode} errorMessage=${p1?.errorMessage}")
        }

        override fun onAdLoaded(p0: Ad?) {
            adView.layoutParams = LinearLayout.LayoutParams(width, heightPx)
            layout.addView(adView)
            listener.invoke(layout, LOADED_AD)
        }

        override fun onAdClicked(p0: Ad?) {

        }

        override fun onLoggingImpression(p0: Ad?) {

        }
    }).build())
}


fun Activity.requestAppLovinBanner(
    placement: String,
    callBack: (layout: LinearLayout?, status: String) -> Unit
) {
    val adView = MaxAdView(placement, this)
    adView.setListener(object : MaxAdViewAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            val layout = LinearLayout(this@requestAppLovinBanner)
            layout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            layout.orientation = LinearLayout.VERTICAL
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val heightPx = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._45sdp)
            adView.layoutParams = LinearLayout.LayoutParams(width, heightPx)
            layout.addView(adView)
            callBack.invoke(layout, LOADED_AD)
        }

        override fun onAdDisplayed(ad: MaxAd?) {

        }

        override fun onAdHidden(ad: MaxAd?) {

        }

        override fun onAdClicked(ad: MaxAd?) {

        }

        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            callBack.invoke(
                null,
                "error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}"
            )
        }

        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
            callBack.invoke(
                null,
                "error code =${error?.code} message=${error?.message} mediatedNetworkErrorCode=${error?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${error?.mediatedNetworkErrorMessage}"
            )
        }

        override fun onAdExpanded(ad: MaxAd?) {

        }

        override fun onAdCollapsed(ad: MaxAd?) {

        }
    })
    adView.loadAd()
}

