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
            layout
        ) {
            if (it == LOADED_AD) {
                sb.append("Google native ads loaded\n")
                binding?.loadingTitle?.text = "Facebook native ads loading..."
                layout.visibility = View.VISIBLE
                binding?.facebookNative?.let {
                    facebookNative(layout)
                }
            } else {
                sb.append("Google native ads error $it\n")
                binding?.loadingTitle?.text = "Facebook native ads loading..."
                binding?.facebookNative?.let {
                    facebookNative(layout)
                }
            }
        }
    }

    private fun facebookNative(layout: LinearLayout) {
        requestNativeFacebook(
            "YOUR_PLACEMENT_ID",
            R.drawable.btn_ads,
            R.color.black,
            layout
        ) {
            if (it == LOADED_AD) {
                binding?.loadingTitle?.text = "Applovin native ads loading..."
                layout.visibility = View.VISIBLE
                binding?.applovinNative?.let {
                }
                sb.append("Facebook native ads loaded\n")
            } else {
                binding?.loadingTitle?.text = "Applovin native ads loading..."
                binding?.applovinNative?.let {
                }
                sb.append("Facebook native ads error =$it\n")
            }
        }
    }

}