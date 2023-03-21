package com.example.lib_common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

/**
 * Created by wangfei44 on 2023/3/20.
 */
object NetUtils {
    fun isNetworkOn(context: Context?): Boolean {
        if (null == context) {
            Log.e(this::class.java.name, "isNetworkOn :: context = null")
            return false
        }
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connMgr.activeNetworkInfo
        return null != activeNetworkInfo && activeNetworkInfo.isConnectedOrConnecting
    }
}