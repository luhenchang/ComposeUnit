package com.example.composeunit.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExitComposeScreen() {
    val context = LocalContext.current
    BackHandler(onBack = { // 监听返回按钮事件
        (context as Activity).finish()
    })
}