package com.afgi.demo

import android.app.Application
import android.content.ContentValues
import android.util.Log
import com.afgi.lib.initInMobi
import com.afgi.lib.initialize
import com.afgi.lib.testDeviceIds
import com.afgi.lib.testDeviceIdsFacebook
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initialize()

        initInMobi("1670153853003")

        AppOpenManager(this)
        testDeviceIdsFacebook("92f7d8c8-a872-42db-8d9d-d8277057d19d")
//        testDeviceIdsFacebook("39b39339-d312-4031-bf14-c97a82f1c4a3")
//        testDeviceIdsFacebook("3aaa49c4-f36c-4704-81c1-beb99ec0c88f")
        testDeviceIds(
            listOf(
                "2B1C398D39E69EF758154530678FA16F"
            )
        )


    }
}