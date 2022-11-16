package com.afgi.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.afgi.*
import com.afgi.lib.*
import com.afgi.myapplication.R
import com.afgi.myapplication.databinding.ActivityNativeBinding

class NativeActivity : AppCompatActivity() {
    var binding: ActivityNativeBinding? = null
    val sb = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.loadingTitle?.text = "Google native ads loading..."
        binding?.googleNative?.let {
            googleNative(it)
        }


    }

    private fun googleNative(layout: LinearLayout) {
        requestNative(
            idNative,
            R.drawable.btn_ads,
            R.color.black,
            layout,
            object : NativeCallBack {
                override fun onLoaded() {
                    sb.append("Google native ads loaded\n")
                    binding?.loadingTitle?.text = "Facebook native ads loading..."
                    layout.visibility = View.VISIBLE
                    binding?.facebookNative?.let {
                        facebookNative(it)
                    }

                }

                override fun onError(error: String) {
                    sb.append("Google native ads error $error\n")
                    binding?.loadingTitle?.text = "Facebook native ads loading..."
                    binding?.facebookNative?.let {
                        facebookNative(it)
                    }
                }
            })
    }

    private fun facebookNative(layout: LinearLayout) {
        requestNativeFacebook("YOUR_PLACEMENT_ID",
            R.drawable.btn_ads,
            R.color.black,
            layout,
            object : NativeCallBack {
                override fun onLoaded() {
                    binding?.loadingTitle?.text = "Applovin native ads loading..."
                    layout.visibility = View.VISIBLE
                    binding?.applovinNative?.let {
                        appLovinNative(it)
                    }
                    sb.append("Facebook native ads loaded\n")
                }

                override fun onError(error: String) {
                    binding?.loadingTitle?.text = "Applovin native ads loading..."
                    binding?.applovinNative?.let {
                        appLovinNative(it)
                    }
                    sb.append("Facebook native ads error =$error\n")
                }
            })
    }

    private fun appLovinNative(layout: LinearLayout) {
        requestNativeApplovin("id", layout, object : NativeCallBack {
            override fun onLoaded() {
                sb.append("Applovin native ads loaded\n")
                binding?.progress?.visibility = View.INVISIBLE
                binding?.loadingTitle?.text = sb.toString()
            }

            override fun onError(error: String) {
                sb.append("Applovin native ads error =$error \n")
                binding?.progress?.visibility = View.INVISIBLE
                binding?.loadingTitle?.text = sb.toString()
            }
        })
    }
}