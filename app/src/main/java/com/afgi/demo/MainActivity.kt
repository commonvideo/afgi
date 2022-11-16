package com.afgi.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afgi.lib.initIronSource
import com.afgi.lib.onPauseIronSource
import com.afgi.lib.onResumeIronSource
import com.afgi.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //todo add in your Activity
    override fun onResume() {
        super.onResume()
        onResumeIronSource()
    }

    //todo add in your Activity
    override fun onPause() {
        super.onPause()
        onPauseIronSource()
    }

    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //TODO  IronSource app id init
        initIronSource("173539a85")

        setContentView(binding?.root)

        binding?.nativeAds?.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    NativeActivity::class.java
                )
            )
        }
        binding?.fullScreen?.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    FullAdsActivity::class.java
                )
            )
        }
        binding?.banner?.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    BannerActivity::class.java
                )
            )
        }
        binding?.appOpen?.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AppOpenActivity::class.java
                )
            )
        }

//        int()
        /*val adsContainerLarge: LinearLayout = findViewById(R.id.adsContainerLarge)
        val banner: LinearLayout = findViewById(R.id.adsContainer)
        val adsContainerNative: LinearLayout = findViewById(R.id.adsContainerNative)
        val adsContainerFb: LinearLayout = findViewById(R.id.adsContainerFb)


        //todo full screen ads
        request(idFullScreen, object : FullCallBack {
            override fun onError() {

            }
        })

        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (isLoaded()) {
                    show(object : CallBack {
                        override fun onCompleted() {

                        }
                    })
                } else {
                    handler.postDelayed(this, 250)
                }
            }
        }
        handler.postDelayed(runnable, 250)

        //todo App open
        requestAppOpen(idAppOpen, object : AppOpenCallBack {
            override fun onLoaded() {
                if (isLoadedAppOpen()) {
                    showAppOpen(object : CallBack {
                        override fun onCompleted() {

                        }
                    })
                }
            }

            override fun onError() {

            }
        })

        //todo native
        requestNative(idNative,
            R.drawable.btn_ads,
            R.color.black,
            adsContainerLarge,
            object : NativeCallBack {
                override fun onLoaded() {
                    adsContainerLarge.visibility = View.VISIBLE
                }

                override fun onError() {

                }
            })


        //todo Banner
        requestBanner(
            idBanner,
            banner,
            object : BannerCallBack {
                override fun onLoaded() {
                    banner.visibility = View.VISIBLE
                }

                override fun onError() {

                }
            })

        //todo native banner
        requestNativeBanner(
            idNative,
            adsContainerNative,
            R.drawable.btn_ads,
            R.color.black,
            object : BannerCallBack {
                override fun onLoaded() {
                    adsContainerNative.visibility = View.VISIBLE
                }

                override fun onError() {

                }
            })

        requestFacebookBanner("YOUR_PLACEMENT_ID", adsContainerFb, object : BannerCallBack {
            override fun onLoaded() {
                adsContainerFb.visibility = View.VISIBLE
                Log.e("requestFacebookBanner", "requestFacebookBanner ---->> onLoaded")
            }

            override fun onError() {
                Log.e("requestFacebookBanner", "requestFacebookBanner ---->> onError")
            }
        })

        //todo native Facebook
        requestNativeFacebook("YOUR_PLACEMENT_ID",
            R.drawable.btn_ads,
            R.color.black,
            adsContainerLarge,
            object : NativeCallBack {
                override fun onLoaded() {
                    adsContainerLarge.visibility = View.VISIBLE
                }

                override fun onError() {

                }
            })


        //todo full screen ads facebook
        requestFacebook("YOUR_PLACEMENT_ID",
            object : FullCallBack {
                override fun onError() {
                    Log.e("requestFacebook", "requestFacebook ---->> onError")
                }
            })


        val handlerFb = Handler(Looper.getMainLooper())
        val runnableFb: Runnable = object : Runnable {
            override fun run() {
                if (isLoadedFacebook()) {
                    showFacebook()
                } else {
                    handlerFb.postDelayed(this, 250)
                }
            }
        }
        handlerFb.postDelayed(runnableFb, 250)*/
    }


}