package com.afgi.lib

interface NativeCallBack {
    fun onLoaded()
    fun onError(error:String)
}