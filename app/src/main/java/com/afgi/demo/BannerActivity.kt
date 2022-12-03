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

    }

    private fun googleBanner(layout: LinearLayout) {
        requestBanner(
            idBanner,
            layout
        ) {
            if (it == LOADED_AD) {
                sb.append("Google Banner loaded\n")
                binding?.loadingTitle?.text = "Loading Google Native Banner"
                binding?.bannerGoogleNativeAds?.let { googleNative(layout) }
            } else {
                binding?.loadingTitle?.text = "Loading Google Native Banner"
                binding?.bannerGoogleNativeAds?.let { googleNative(layout) }
                sb.append("Google Banner error=$it \n")
            }
        }
    }

    private fun googleNative(layout: LinearLayout) {
        requestNativeBanner(
            idNative,
            layout,
            com.afgi.myapplication.R.drawable.btn_ads,
            com.afgi.myapplication.R.color.black
        ) {
            if (it == LOADED_AD) {
                sb.append("Google Native Banner loaded\n")
                binding?.loadingTitle?.text = "Loading Facebook Banner"
                binding?.bannerFacebookAds?.let { facebookBanner(layout) }
            } else {
                binding?.loadingTitle?.text = "Loading Facebook Banner"
                binding?.bannerFacebookAds?.let { facebookBanner(layout) }
                sb.append("Google Native Banner error =$it \n")
            }
        }
    }

    private fun facebookBanner(layout: LinearLayout) {
        requestFacebookBanner("YOUR_PLACEMENT_ID", layout) {
            if (it == LOADED_AD) {
                sb.append("Facebook Banner loaded\n")
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
            } else {
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
                sb.append("Facebook Banner error=$it \n")
            }
        }
    }

}