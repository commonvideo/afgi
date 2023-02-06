package com.afgi.lib

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.facebook.ads.*
import com.facebook.ads.AdError
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.NativeAdEventListener


//0= button color 1=button color 2=title color 3=body title color
fun Activity.requestNative(
    vararg color: Int,
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    if (color.size == 4) {
        var layout: LinearLayout? = null

        AdLoader.Builder(
            this,
            placement
        )
            .forNativeAd { nativeAd ->

                layout = LinearLayout(this)
                layout?.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                layout?.orientation = LinearLayout.VERTICAL

                val adView = layoutInflater
                    .inflate(R.layout.ad_unified_large, null) as NativeAdView

                val btn = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
                val adHeadline = adView.findViewById<AppCompatTextView>(R.id.ad_headline)
                val adBody = adView.findViewById<AppCompatTextView>(R.id.ad_body)

                btn.setBackgroundResource(color[0])
                btn.setTextColor(ContextCompat.getColor(this, color[1]))

                adHeadline.setTextColor(ContextCompat.getColor(this, color[2]))

                adBody.setTextColor(ContextCompat.getColor(this, color[3]))

                populateUnifiedNativeAdViewLarge(nativeAd, adView)
                layout?.addView(adView)
                adView.bringToFront()
                layout?.invalidate()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    listener.invoke(null, loadAdError.toString())
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener.invoke(layout, LOADED_AD)
                }
            })
            .build()
            .loadAd(AdRequest.Builder().build())
    } else {
        listener.invoke(null, "Color array must be size 4")
    }
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
    vararg color: Int,
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    val nativeAd = com.facebook.ads.NativeAd(this, placement)
    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(object : NativeAdListener {
        override fun onError(p0: Ad?, p1: AdError?) {
            listener.invoke(null, "errorCode= ${p1?.errorCode} errorMessage=${p1?.errorMessage}")
        }

        override fun onAdLoaded(ad: Ad?) {
            if (nativeAd != ad) {
                listener.invoke(null, "error")
                return
            }
            val layout = LinearLayout(this@requestNativeFacebook)
            layout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            layout.orientation = LinearLayout.VERTICAL
            inflateAd(nativeAd, layout, color) {
                if (it == LOADED_AD) {
                    listener.invoke(layout, it)
                } else {
                    listener.invoke(null, it)
                }
            }

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
    layout: LinearLayout,
    color: IntArray,
    listener: (status: String) -> Unit
) {

    nativeAd.unregisterView()
    val nativeAdLayout = NativeAdLayout(this)
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
            Log.e("error", "NullPointerException")
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

        nativeAdCallToAction.setBackgroundResource(color[0])
        nativeAdCallToAction.setTextColor(ContextCompat.getColor(this, color[1]))
        nativeAdTitle.setTextColor(ContextCompat.getColor(this, color[2]))

        nativeAdBody.setTextColor(ContextCompat.getColor(this, color[3]))

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


//0= button color 1=button color 2=title color 3=body title color
fun Activity.requestNativeHome(
    vararg color: Int,
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    if (color.size == 4) {
        var layout: LinearLayout? = null

        AdLoader.Builder(
            this,
            placement
        )
            .forNativeAd { nativeAd ->

                layout = LinearLayout(this)
                layout?.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                layout?.orientation = LinearLayout.VERTICAL

                val adView = layoutInflater
                    .inflate(R.layout.ad_unified_large_home, null) as NativeAdView

                val btn = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
                val adHeadline = adView.findViewById<AppCompatTextView>(R.id.ad_headline)
                val adBody = adView.findViewById<AppCompatTextView>(R.id.ad_body)

                btn.setBackgroundResource(color[0])
                btn.setTextColor(ContextCompat.getColor(this, color[1]))

                adHeadline.setTextColor(ContextCompat.getColor(this, color[2]))

                adBody.setTextColor(ContextCompat.getColor(this, color[3]))

                populateUnifiedNativeAdViewLargeHome(nativeAd, adView)
                layout?.addView(adView)
                adView.bringToFront()
                layout?.invalidate()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    listener.invoke(null, loadAdError.toString())
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener.invoke(layout, LOADED_AD)
                }
            })
            .build()
            .loadAd(AdRequest.Builder().build())
    } else {
        listener.invoke(null, "Color array must be size 4")
    }
}

private fun populateUnifiedNativeAdViewLargeHome(nativeAd: NativeAd, adView: NativeAdView) {
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


fun Activity.requestNativeFacebookHome(
    vararg color: Int,
    placement: String,
    listener: (layout: LinearLayout?, status: String) -> Unit
) {
    val nativeAd = com.facebook.ads.NativeAd(this, placement)
    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(object : NativeAdListener {
        override fun onError(p0: Ad?, p1: AdError?) {
            listener.invoke(null, "errorCode= ${p1?.errorCode} errorMessage=${p1?.errorMessage}")
        }

        override fun onAdLoaded(ad: Ad?) {
            if (nativeAd != ad) {
                listener.invoke(null, "error")
                return
            }
            val layout = LinearLayout(this@requestNativeFacebookHome)
            layout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            layout.orientation = LinearLayout.VERTICAL
            inflateAdHome(nativeAd, layout, color) {
                if (it == LOADED_AD) {
                    listener.invoke(layout, it)
                } else {
                    listener.invoke(null, it)
                }
            }

        }

        override fun onAdClicked(p0: Ad?) {
        }

        override fun onLoggingImpression(p0: Ad?) {
        }

        override fun onMediaDownloaded(p0: Ad?) {
        }
    }).build())
}

fun Activity.inflateAdHome(
    nativeAd: com.facebook.ads.NativeAd,
    layout: LinearLayout,
    color: IntArray,
    listener: (status: String) -> Unit
) {

    nativeAd.unregisterView()
    val nativeAdLayout = NativeAdLayout(this)
    // Add the Ad view into the ad container.
    val inflater = LayoutInflater.from(this)
    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
    val adView: LinearLayout =
        inflater.inflate(
            R.layout.ads_native_facebook_home,
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
            Log.e("error", "NullPointerException")
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

        nativeAdCallToAction.setBackgroundResource(color[0])
        nativeAdCallToAction.setTextColor(ContextCompat.getColor(this, color[1]))
        nativeAdTitle.setTextColor(ContextCompat.getColor(this, color[2]))

        nativeAdBody.setTextColor(ContextCompat.getColor(this, color[3]))

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


fun Activity.requestNativeApplovin(
    id: String,
    callBack: (layout: LinearLayout?, status: String) -> Unit
) {
    val nativeAdLoader = MaxNativeAdLoader(id, this)
    var loadedNativeAd: MaxAd? = null
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
        override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
            super.onNativeAdLoaded(p0, p1)
            val layout = LinearLayout(this@requestNativeApplovin)
            layout.layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            layout.orientation = LinearLayout.VERTICAL
            if (loadedNativeAd != null) {
                nativeAdLoader.destroy(loadedNativeAd)
            }
            loadedNativeAd = p1
            layout.addView(p0)
            callBack.invoke(layout, LOADED_AD)
        }

        override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
            super.onNativeAdLoadFailed(p0, p1)
            callBack.invoke(
                null,
                "error code =${p1?.code} message=${p1?.message} mediatedNetworkErrorCode=${p1?.mediatedNetworkErrorCode}  mediatedNetworkErrorMessage=${p1?.mediatedNetworkErrorMessage}"
            )
        }

        override fun onNativeAdClicked(p0: MaxAd?) {
            super.onNativeAdClicked(p0)
        }

        override fun onNativeAdExpired(p0: MaxAd?) {
            super.onNativeAdExpired(p0)
        }
    })
    nativeAdLoader.loadAd()
}

fun Activity.requestNativeInMobi(
    id: String,
    callBack: (layout: LinearLayout?, status: String) -> Unit
) {

    var nativeAd: InMobiNative? = null

    val inmobinativeadlistener = object : NativeAdEventListener() {
        override fun onAdLoadSucceeded(p0: InMobiNative, p1: AdMetaInfo) {
            super.onAdLoadSucceeded(p0, p1)

            val layout = LinearLayout(this@requestNativeInMobi)

            layout.addView(
                nativeAd?.getPrimaryViewOfWidth(
                    this@requestNativeInMobi,
                    layout,
                    layout,
                    1080
                )
            )

            callBack.invoke(layout, LOADED_AD)
        }

        override fun onAdLoadFailed(p0: InMobiNative, p1: InMobiAdRequestStatus) {
            super.onAdLoadFailed(p0, p1)

            Log.e("TAG", "onAdLoadFailed p0: ${p0.adTitle}")
            Log.e("TAG", "onAdLoadFailed p1: ${p1.statusCode}")

        }
    }


    nativeAd = InMobiNative(this, id.toLong(), inmobinativeadlistener)

    nativeAd.load()
}

