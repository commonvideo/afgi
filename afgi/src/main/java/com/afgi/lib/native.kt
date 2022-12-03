package com.afgi.lib

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.facebook.ads.*
import com.facebook.ads.AdError
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

fun Activity.requestNative(
    placement: String, btnColor: Int,
    btnTxtColor: Int, layout: LinearLayout, listener: (str: String) -> Unit
) {
    AdLoader.Builder(
        this,
        placement
    )
        .forNativeAd { nativeAd ->
            val adView = layoutInflater
                .inflate(R.layout.ad_unified_large, null) as NativeAdView

            val btn = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
            btn.setBackgroundResource(btnColor)
            btn.setTextColor(btnTxtColor)

            populateUnifiedNativeAdViewLarge(nativeAd, adView)
            layout.removeAllViews()
            layout.addView(adView)
            adView.bringToFront()
            layout.invalidate()
        }
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                listener.invoke(loadAdError.toString())
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                listener.invoke(LOADED_AD)
            }
        })
        .build()
        .loadAd(AdRequest.Builder().build())
}

private fun populateUnifiedNativeAdViewLarge(nativeAd: NativeAd, adView: NativeAdView) {
    val vc = nativeAd.mediaContent
    vc!!.videoController.videoLifecycleCallbacks = object : VideoLifecycleCallbacks() {
        override fun onVideoEnd() {
            super.onVideoEnd()
        }
    }
    val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
    adView.mediaView = mediaView
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
    adView.priceView = adView.findViewById(R.id.ad_price)
    adView.starRatingView = adView.findViewById(R.id.ad_stars)
    adView.storeView = adView.findViewById(R.id.ad_store)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

    // Some assets are guaranteed to be in every UnifiedNativeAd.
    (adView.headlineView as AppCompatTextView?)!!.text = nativeAd.headline
    (adView.bodyView as AppCompatTextView?)!!.text = nativeAd.body
    (adView.callToActionView as AppCompatButton?)!!.text = nativeAd.callToAction

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.icon == null) {
        adView.iconView!!.visibility = View.GONE
    } else {
        (adView.iconView as ImageView?)!!.setImageDrawable(
            nativeAd.icon!!.drawable
        )
        adView.iconView!!.visibility = View.VISIBLE
    }
    if (nativeAd.price == null) {
        adView.priceView!!.visibility = View.GONE
    } else {
        adView.priceView!!.visibility = View.VISIBLE
        (adView.priceView as AppCompatTextView?)!!.text = nativeAd.price
    }
    if (nativeAd.store == null) {
        adView.storeView!!.visibility = View.GONE
    } else {
        adView.storeView!!.visibility = View.VISIBLE
        (adView.storeView as AppCompatTextView?)!!.text = nativeAd.store
    }
    if (nativeAd.starRating == null) {
        adView.starRatingView!!.visibility = View.GONE
    } else {
        (adView.starRatingView as RatingBar?)
            ?.setRating(nativeAd.starRating!!.toFloat())
        adView.starRatingView!!.visibility = View.VISIBLE
    }
    if (nativeAd.advertiser == null) {
        adView.advertiserView!!.visibility = View.GONE
    } else {
        (adView.advertiserView as AppCompatTextView?)!!.text = nativeAd.advertiser
        adView.advertiserView!!.visibility = View.VISIBLE
    }
    adView.setNativeAd(nativeAd)
}


fun Activity.requestNativeFacebook(
    placement: String,
    btnColor: Int,
    btnTxtColor: Int,
    layout: LinearLayout,
    listener: (str: String) -> Unit
) {
    val nativeAd = com.facebook.ads.NativeAd(this, placement)
    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(object : NativeAdListener {
        override fun onError(p0: Ad?, p1: AdError?) {
            listener.invoke("errorCode= ${p1?.errorCode} errorMessage=${p1?.errorMessage}")
        }

        override fun onAdLoaded(ad: Ad?) {
            if (nativeAd != ad) {
                return
            }
            inflateAd(nativeAd, btnColor, btnTxtColor, layout, listener)

        }

        override fun onAdClicked(p0: Ad?) {
        }

        override fun onLoggingImpression(p0: Ad?) {
        }

        override fun onMediaDownloaded(p0: Ad?) {
        }
    }).build())
}

fun Activity.inflateAd(
    nativeAd: com.facebook.ads.NativeAd,
    btnColor: Int,
    btnTxtColor: Int,
    layout: LinearLayout,
    listener: (str: String) -> Unit
) {

    nativeAd.unregisterView()
    var nativeAdLayout = NativeAdLayout(this)
    // Add the Ad view into the ad container.
    val inflater = LayoutInflater.from(this)
    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
    val adView: LinearLayout =
        inflater.inflate(
            R.layout.ads_native_facebook,
            nativeAdLayout,
            false
        ) as LinearLayout

    // Add the AdOptionsView
    try {
        val adChoicesContainer =
            adView.findViewById<LinearLayout>(R.id.adChoicesContainerLib)
        val adOptionsView = AdOptionsView(this, nativeAd, nativeAdLayout)
        try {
            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)
        } catch (e: NullPointerException) {
        }


        // Create native UI using the ad metadata.
        val nativeAdIcon: com.facebook.ads.MediaView =
            adView.findViewById(R.id.nativeAdIconLib)
        val nativeAdTitle = adView.findViewById<AppCompatTextView>(R.id.nativeAdTitleLib)
        val nativeAdMedia: com.facebook.ads.MediaView =
            adView.findViewById(R.id.nativeAdMediaLib)
        /*TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);*/
        val nativeAdBody = adView.findViewById<AppCompatTextView>(R.id.nativeAdBodyLib)
        val sponsoredLabel =
            adView.findViewById<AppCompatTextView>(R.id.nativeAdSponsoredLabelLib)
        val nativeAdCallToAction =
            adView.findViewById<AppCompatButton>(R.id.nativeAdCallToActionLib)

        nativeAdCallToAction.setBackgroundResource(btnColor)
        nativeAdCallToAction.setTextColor(btnTxtColor)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        /*nativeAdSocialContext.setText(nativeAd.getAdSocialContext());*/
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = java.util.ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adView,
            nativeAdMedia,
            nativeAdIcon,
            clickableViews
        )

        nativeAdLayout.addView(adView)
        layout.addView(nativeAdLayout)

        listener.invoke(LOADED_AD)
    } catch (e: NullPointerException) {
        e.printStackTrace()
        listener.invoke("facebook $e")
    }
}