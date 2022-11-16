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
            layout,
            object : BannerCallBack {
                override fun onLoaded() {
                    sb.append("Google Banner loaded\n")
                    binding?.loadingTitle?.text = "Loading Google Native Banner"
                    binding?.bannerGoogleNativeAds?.let { googleNative(it) }
                }

                override fun onError(error: String) {
                    binding?.loadingTitle?.text = "Loading Google Native Banner"
                    binding?.bannerGoogleNativeAds?.let { googleNative(it) }
                    sb.append("Google Banner error=$error \n")
                }
            })

    }

    private fun googleNative(layout: LinearLayout) {
        requestNativeBanner(
            idNative,
            layout,
            com.afgi.myapplication.R.drawable.btn_ads,
            com.afgi.myapplication.R.color.black,
            object : BannerCallBack {
                override fun onLoaded() {
                    sb.append("Google Native Banner loaded\n")
                    binding?.loadingTitle?.text = "Loading Facebook Banner"
                    binding?.bannerFacebookAds?.let { facebookBanner(it) }
                }

                override fun onError(error: String) {
                    binding?.loadingTitle?.text = "Loading Facebook Banner"
                    binding?.bannerFacebookAds?.let { facebookBanner(it) }
                    sb.append("Google Native Banner error =$error \n")
                }
            })
    }

    private fun facebookBanner(layout: LinearLayout) {
        requestFacebookBanner("YOUR_PLACEMENT_ID", layout, object : BannerCallBack {
            override fun onLoaded() {
                sb.append("Facebook Banner loaded\n")
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
                binding?.bannerApplovinAds?.let { applovineBanner(it) }
            }

            override fun onError(error: String) {
                binding?.loadingTitle?.text = "Loading  Applovine Banner"
                binding?.bannerApplovinAds?.let { applovineBanner(it) }
                sb.append("Facebook Banner error=$error \n")
            }
        })
    }

    private fun applovineBanner(layout: LinearLayout) {
        requestAppLovinBanner("id", layout, object : BannerCallBack {
            override fun onLoaded() {
                sb.append("Applovine Banner loaded\n")
                binding?.loadingTitle?.text = sb.toString()
                binding?.progress?.visibility = View.GONE
            }

            override fun onError(error: String) {
                sb.append("Applovine Banner onError=$error\n")
                binding?.loadingTitle?.text = sb.toString()
                binding?.progress?.visibility = View.GONE
            }
        })
    }

}