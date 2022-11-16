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

        binding?.google?.setOnClickListener {
            binding?.loadingText?.text = "Google Loading..."
            request(idFullScreen, isExitAds = false, object : FullCallBack {
                override fun onError(error: String) {
                    binding?.loadingText?.text = error
                }
            })
            showAd()
        }
        binding?.facebook?.setOnClickListener {
            binding?.loadingText?.text = "Facebook Loading..."
            requestFacebook("YOUR_PLACEMENT_ID", object : FullCallBack {
                override fun onError(error: String) {
                    binding?.loadingText?.text = error
                }
            })
            showAd()
        }
        binding?.ironsource?.setOnClickListener {
            binding?.loadingText?.text = "IronSource Loading..."
            requestIronSource(object : FullCallBack {
                override fun onError(error: String) {
                    binding?.loadingText?.text = error
                }
            })
            showAd()
        }
        binding?.applovine?.setOnClickListener {
            binding?.loadingText?.text = "Applovine Loading..."
            requestApplovine("id",isExitAds = false, object : FullCallBack {
                override fun onError(error: String) {
                    binding?.loadingText?.text = error
                }
            })
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
                    show("", object : CallBack {
                        override fun onCompleted() {
                            binding?.loadingText?.text = ""
                        }
                    })
                } else {
                    handler.postDelayed(this, 250)
                }
            }
        }
        handler.postDelayed(runnable, 250)
    }
}