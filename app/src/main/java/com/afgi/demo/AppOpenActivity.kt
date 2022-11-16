package com.afgi.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.afgi.lib.*
import com.afgi.myapplication.databinding.ActivityAppOpenBinding

class AppOpenActivity : AppCompatActivity() {
    var binding: ActivityAppOpenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppOpenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.googleAppOpen?.setOnClickListener {
            binding?.loadingText?.text="Google appopn loading..."
            requestAppOpen(idAppOpen, object : AppOpenCallBack {
                override fun onLoaded() {
                    binding?.loadingText?.text=""
                }

                override fun onError(error: String) {
                    binding?.loadingText?.text=error
                }
            })
            show()
        }
        binding?.applovinAppOpen?.setOnClickListener {
            binding?.loadingText?.text="Applovine appopn loading..."
            requestApplovinAppOpen("id", object : AppOpenCallBack {
                override fun onLoaded() {
                    binding?.loadingText?.text=""
                }

                override fun onError(error: String) {
                    binding?.loadingText?.text=error
                }
            })
            show()
        }
    }

    fun show() {
        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (isLoadedAppOpen()) {
                    showAppOpen(object : CallBack {
                        override fun onCompleted() {

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