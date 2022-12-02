package com.afgi.lib

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.applovin.sdk.AppLovinSdk
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var idNative = "/6499/example/native"
var idBanner = "/6499/example/banner"
var idFullScreen = "/6499/example/interstitial"
var idAppOpen = "/6499/example/app-open"

fun Context.initApplovin() {
    AppLovinSdk.getInstance(this).mediationProvider = "max"
    AppLovinSdk.getInstance(this).initializeSdk {}
}

fun Context.initialize() {
    MobileAds.initialize(this)
    AudienceNetworkAds.initialize(this)
}

fun testDeviceIdsFacebook(ids: String) {
    AdSettings.addTestDevice(ids)
}

fun testDeviceIds(ids: List<String>) {
    val builder = RequestConfiguration.Builder().setTestDeviceIds(ids).build()
    MobileAds.setRequestConfiguration(builder)
}

/*fun Activity.onPauseIronSource() {
    IronSource.onPause(this)
}

fun Activity.onResumeIronSource() {
    IronSource.onResume(this)

}*/


fun Activity.initIronSource(appId: String) {
    /* IntegrationHelper.validateIntegration(this)
     val sharedPreferences = getSharedPreferences("advertisingId", Context.MODE_PRIVATE)

     if (sharedPreferences.getString("id", "null") != "null") {
         IronSource.setUserId(sharedPreferences.getString("id", "userId") ?: "userId")
         IronSource.init(
             this@initIronSource, appId
         ) { Log.e("initIronSource ", " onInitializationComplete") }
     } else {
         CoroutineScope(Dispatchers.IO).launch {
             var advId = IronSource.getAdvertiserId(this@initIronSource)
             if (TextUtils.isEmpty(advId)) {
                 advId = "userId"
             }
             val editor = sharedPreferences.edit()
             editor.putString("id", advId)
             editor.apply()
             IronSource.setUserId(advId)
             IronSource.init(
                 this@initIronSource, appId
             ) { Log.e("initIronSource ", " onInitializationComplete") }
         }
     }*/
}
