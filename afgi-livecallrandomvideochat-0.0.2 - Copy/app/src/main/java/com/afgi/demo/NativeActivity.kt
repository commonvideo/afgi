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

    //0= button color 1=button color 2=title color 3=body title color
    val listColor = intArrayOf(R.color.purple_200, R.color.white, R.color.black, R.color.black)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        /*binding?.loadingTitle?.text = "Google native ads loading..."
        binding?.googleNative?.let {
            googleNative(it)
        }*/

        requestNativeApplovin(
            "86454a677b7f9ff4"
        ) { layout, status ->
            if (status == LOADED_AD) {
                binding?.applovinNative?.visibility = View.VISIBLE
                binding?.applovinNative?.removeAllViews()
                binding?.applovinNative?.addView(layout)
                sb.append("applovine native ads loaded\n")
                binding?.loadingTitle?.text = sb.toString()
            } else {
                sb.append("applovine native ads error =$status\n")
                binding?.loadingTitle?.text = sb.toString()
            }
        }

//        requestNativeInMobi(idInMobiNative) { layout, status ->
//            if (status == LOADED_AD) {
//                binding?.inmobiNative?.visibility = View.VISIBLE
//                binding?.inmobiNative?.removeAllViews()
//                binding?.inmobiNative?.addView(layout)
//                sb.append("inmobi native ads loaded\n")
//                binding?.loadingTitle?.text = sb.toString()
//            } else {
//                sb.append("inmobi native ads error =$status\n")
//                binding?.loadingTitle?.text = sb.toString()
//            }
//        }




    }

    private fun googleNative(layoutMain: LinearLayout) {

        requestNative(color = listColor, idNative) { layout, status ->
            if (status == LOADED_AD) {
                sb.append("Google native ads loaded\n")
                binding?.loadingTitle?.text = "Facebook native ads loading..."
                layoutMain.visibility = View.VISIBLE
                layoutMain.removeAllViews()
                layoutMain.addView(layout)
                binding?.facebookNative?.let {
                    facebookNative(layoutMain)
                }
            } else {
                sb.append("Google native ads error $status\n")
                binding?.loadingTitle?.text = "Facebook native ads loading..."
                binding?.facebookNative?.let {
                    facebookNative(layoutMain)
                }
            }
        }
    }

    private fun facebookNative(layoutMain: LinearLayout) {
        requestNativeFacebook(
            color = listColor,
            "YOUR_PLACEMENT_ID"
        ) { layout, status ->
            if (status == LOADED_AD) {
                layoutMain.visibility = View.VISIBLE
                layoutMain.removeAllViews()
                layoutMain.addView(layout)
                binding?.applovinNative?.let {
                    applovineNative(it)
                }
                sb.append("Facebook native ads loaded\n")
                binding?.loadingTitle?.text = sb.toString()

            } else {
                binding?.applovinNative?.let {
                }
                sb.append("Facebook native ads error =$status\n")
                binding?.loadingTitle?.text = sb.toString()
            }
        }
    }

    private fun applovineNative(layoutMain: LinearLayout) {
        requestNativeApplovin(
            "id"
        ) { layout, status ->
            if (status == LOADED_AD) {
                layoutMain.visibility = View.VISIBLE
                layoutMain.removeAllViews()
                layoutMain.addView(layout)
                sb.append("applovine native ads loaded\n")
                binding?.loadingTitle?.text = sb.toString()
            } else {
                sb.append("applovine native ads error =$status\n")
                binding?.loadingTitle?.text = sb.toString()
            }
        }
    }

}