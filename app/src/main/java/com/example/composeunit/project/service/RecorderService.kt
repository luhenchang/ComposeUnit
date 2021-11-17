package com.example.composeunit.project.service

import android.app.*
import android.app.Notification.MediaStyle
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.composeunit.R

/**
 * Created by wangfei44 on 2021/11/16.
 */
class RecorderService : Service() {
    companion object {
        const val ACTION_NAME = "action_type"
        const val ACTION_INVALID = 0
        const val NOTIFICATION_ID = Int.MAX_VALUE - 5
        const val ACTION_RECORD_NOTIFICATION = 9
        const val PARAM_SHOW_NOTIFICATION = "show_notification"
        const val CHANNEL_ID = "recording_channel"
        const val NOTIFICATION_ENTER_RECORDER = 24


    }

    override fun onCreate() {
        super.onCreate()
        if (SystemUtils.isOOrLater()) {
            startForeground(
                NOTIFICATION_ID,
                NotificationCompatHelper.getInstance(applicationContext)?.getBuilder()?.build()
            )
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle == null || !bundle.containsKey(ACTION_NAME))
            return super.onStartCommand(intent, flags, startId)
        when(bundle.getInt(ACTION_NAME, ACTION_INVALID)){
            ACTION_RECORD_NOTIFICATION ->{
                if (SystemUtils.isOOrLater()) {
                    showRecordNotification(bundle.getBoolean(PARAM_SHOW_NOTIFICATION))
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showRecordNotification(show: Boolean) {
        if (show) {
            val mNotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val name: CharSequence = this.getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )
            mNotificationManager.createNotificationChannel(mChannel)
            val style = MediaStyle()
            val launchIntent = Intent(Intent.ACTION_MAIN)
            launchIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            launchIntent.setClassName("com.example.composeunit", "com.example.composeunit.project.SplashActivity")
            launchIntent.putExtra(ACTION_NAME, NOTIFICATION_ENTER_RECORDER)
            val contentIntent = PendingIntent.getActivity(this, 4, launchIntent, 0)

            val mNotificationBuilder =
                Notification.Builder(this,CHANNEL_ID)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setStyle(style)
                    .setSmallIcon(R.drawable.android_icon)
                    .setShowWhen(false)
                    .setContentIntent(contentIntent)
                    .setContentTitle("正在播放")
                    .setContentText("录音机播放")
            val notification = mNotificationBuilder.build()
            notification.tickerText = "21:10"
            startForeground(R.string.notification_playback, notification)
        }else{
            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return RecorderBind()
    }

    class RecorderBind : Binder() {
        val service: RecorderService get() = RecorderService()
    }
}