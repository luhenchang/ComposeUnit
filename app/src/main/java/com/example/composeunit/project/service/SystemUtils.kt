package com.example.composeunit.project.service

import android.os.Build

/**
 * Created by wangfei44 on 2021/11/16.
 */
object SystemUtils {
    @JvmStatic
    fun isOOrLater(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}