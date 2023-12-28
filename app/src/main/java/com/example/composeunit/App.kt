package com.example.composeunit

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.example.composeunit.ui.compose.other.service.RecorderService.Companion.CHANNEL_ID

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            CHANNEL_ID,
            "High priority notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
///提交错误内容