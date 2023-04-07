package com.afgi.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.afgi.*
import com.afgi.lib.*
import com.afgi.myapplication.databinding.ActivityFullAdsBinding

class FullAdsActivity : AppCompatActivity() {
    var binding: ActivityFullAdsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullAdsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.inmobi?.setOnClickListener {
            binding?.loadingText?.text = "InMobi Loading..."
            requestInMobi(idInMobiIntertitial, isExitAds = false) {
                if (it != LOADED_AD) {
                    binding?.loadingText?.text = it
                } else binding?.loadingText?.text = it
            }
            showAd()
        }

        binding?.google?.setOnClickListener {
            binding?.loadingText?.text = "Google Loading..."
            request(idFullScreen, isExitAds = false) {
                if (it != LOADED_AD) {
                    binding?.loadingText?.text = it
                } else binding?.loadingText?.text = it
            }
            showAd()
        }
        binding?.facebook?.setOnClickListener {
            binding?.loadingText?.text = "Facebook Loading..."
            requestFacebook("YOUR_PLACEMENT_ID") {
                if (it != LOADED_AD) {
                    binding?.loadingText?.text = it
                } else binding?.loadingText?.text = it
            }
            showAd()
        }
        binding?.ironsource?.setOnClickListener {
            binding?.loadingText?.text = "IronSource Loading..."
            requestIronSource {
                if (it != LOADED_AD) {
                    binding?.loadingText?.text = it
                } else binding?.loadingText?.text = it
            }
            showAd()
        }
        binding?.applovine?.setOnClickListener {
            binding?.loadingText?.text = "applovine Loading..."
            requestApplovine("id", true) {
                if (it != LOADED_AD) {
                    binding?.loadingText?.text = it
                } else binding?.loadingText?.text = it
            }
            showAd()
        }
    }

    private fun showAd() {
        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            override fun run() {
                Log.e("FullAdsActivity ", "isLoaded =${isLoaded()}")

                if (isLoadedFacebook()) {
                    showFacebook()
                    binding?.loadingText?.text = ""
                } else if (isLoaded()) {
                    //todo param ironsource key
                    show("") {
                        binding?.loadingText?.text = ""
                    }
                } else if (isExitLoaded()) {
                    showExit {
                        binding?.loadingText?.text = ""
                    }
                } else {
                    handler.postDelayed(this, 250)
                }
            }
        }
        handler.postDelayed(runnable, 250)
    }
}