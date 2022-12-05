package com.afgi.demo

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.afgi.lib.*
import com.afgi.myapplication.databinding.ActivityBannerBinding

class BannerActivity : AppCompatActivity() {

    var binding: ActivityBannerBinding? = null
    val sb = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBannerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.loadingTitle?.text = "Loading Google Banner"
        binding?.bannerGoogleAds?.let {
            googleBanner(it)
        }

        requestAppLovinBanner("id") { layout, status ->
            if (status == LOADED_AD) {
                binding?.bannerApplovinAds?.visibility = View.VISIBLE
                binding?.bannerApplovinAds?.removeAllViews()
                binding?.bannerApplovinAds?.addView(layout)
                sb.append("AppLovin Banner loaded\n")
            } else {
                sb.append("AppLovin Banner error=$status \n")
            }
        }

    }

    private fun googleBanner(layoutMain: LinearLayout) {
        requestBanner(
            idBanner
        ) { layout, status ->
            if (status == LOADED_AD) {

                layoutMain.visibility = View.VISIBLE
                layoutMain.removeAllViews()
                layoutMain.addView(layout)

                sb.append("Google Banner loaded\n")
                binding?.loadingTitle?.text = "Loading facebookBanner Banner"
                binding?.bannerGoogleNativeAds?.let { facebookBanner(it) }
            } else {
                binding?.loadingTitle?.text = "Loading facebookBanner Banner"
                binding?.bannerGoogleNativeAds?.let { facebookBanner(it) }
                sb.append("Google Banner error=$status \n")
            }
        }
    }

    private fun facebookBanner(layoutMain: LinearLayout) {
        requestFacebookBanner("YOUR_PLACEMENT_ID") { layout, status ->
            if (status == LOADED_AD) {
                layoutMain.visibility = View.VISIBLE
                layoutMain.removeAllViews()
                layoutMain.addView(layout)
                sb.append("Facebook Banner loaded\n")
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
            } else {
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
                sb.append("Facebook Banner error=$status \n")
            }
        }
    }

}