package com.example.composeunit.project.service

import android.app.*
import android.app.Notification.MediaStyle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.composeunit.R
import com.example.composeunit.project.widget.RecorderAppWidget
import com.example.composeunit.project.widget.UpdateRecorderWidgetService
import com.example.composeunit.project.widget.UpdateRecorderWidgetService.Companion.ACTION_RECORD
import java.lang.Exception

/**
 * Created by wangfei44 on 2021/11/16.
 */
class RecorderService : Service() {
    var TAG = this.javaClass.name

    companion object {
        const val ACTION_NAME = "action_type"
        const val ACTION_INVALID = 0
        const val NOTIFICATION_ID = Int.MAX_VALUE - 5
        const val ACTION_RECORD_NOTIFICATION = 9
        const val PARAM_SHOW_NOTIFICATION = "show_notification"
        const val CHANNEL_ID = "recording_channel"
        const val NOTIFICATION_ENTER_RECORDER = 24

        const val ACTION_CANCEL_TIMER = "android.appwidget.action.APPWIDGET_STOP_RECORDER"
        const val ACTION_RESUME_TIMER = "android.appwidget.action.APPWIDGET_RESUME_RECORDER"

    }

    private var widgetBroadcastReceiver: BroadcastReceiver? = null
    private val countDownTimer = object : MyCountDownTimer(1000 * 100L, 1000L) {
        override fun onTick(millisUntilFinished: Long) {
            //发送广播
            val intent = Intent()
            //指定广播的名字
            intent.action = RecorderAppWidget.UPDATE_ACTION
            //指定广播的内容
            intent.putExtra("time", millisUntilFinished)
            //发送广播
            sendBroadcast(intent)
        }

        override fun onFinish() {
        }

    }

    override fun onCreate() {
        super.onCreate()
        if (SystemUtils.isOOrLater()) {
            startForeground(
                NOTIFICATION_ID,
                NotificationCompatHelper.getInstance(applicationContext)?.getBuilder()?.build()
            )
        }
        registerWidgetReceiver()
    }

    private fun registerWidgetReceiver() {
        if (null == widgetBroadcastReceiver) {
            widgetBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        ACTION_CANCEL_TIMER -> {
                            countDownTimer.pause()
                        }
                        ACTION_RESUME_TIMER ->{
                            countDownTimer.start()
                        }
                    }
                }
            }
        }
        val filter = IntentFilter()
        filter.addAction(ACTION_CANCEL_TIMER)
        filter.addAction(ACTION_RESUME_TIMER)
        // 或者使用Intent.ACTION_HEADSET_PLUG
        // 或者使用Intent.ACTION_HEADSET_PLUG
        try {
            registerReceiver(widgetBroadcastReceiver, filter)
        } catch (e: Exception) {
            Log.e(TAG, "registerWidgetReceiver error ::: $e")
        }
    }

    private fun unregisterWidgetReceiver() {
        if (widgetBroadcastReceiver == null) {
            return
        }
        try {
            unregisterReceiver(widgetBroadcastReceiver)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "unregisterWidgetReceiver error ::: $e")
        }
        widgetBroadcastReceiver = null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle == null || !bundle.containsKey(ACTION_NAME))
            return super.onStartCommand(intent, flags, startId)
        when (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
            ACTION_RECORD_NOTIFICATION -> {
                Log.i("onStartCommand::bundle=", "开始呢么？")
                countDownTimer.start()
                if (SystemUtils.isOOrLater()) {
                    //showRecordNotification(bundle.getBoolean(PARAM_SHOW_NOTIFICATION))
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showRecordNotification(show: Boolean) {
        if (show) {
            val mNotificationManager =
                this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            launchIntent.setClassName(
                "com.example.composeunit",
                "com.example.composeunit.project.SplashActivity"
            )
            launchIntent.putExtra(ACTION_NAME, NOTIFICATION_ENTER_RECORDER)
            val contentIntent = PendingIntent.getActivity(this, 4, launchIntent, 0)

            val mNotificationBuilder =
                Notification.Builder(this, CHANNEL_ID)
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
        } else {
            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return RecorderBind()
    }

    class RecorderBind : Binder() {
        val service: RecorderService get() = RecorderService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterWidgetReceiver()
    }

    open class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        private var mRemainingTime: Long = 0
        private var mInterval: Long = 0

        init {
            this.mInterval = countDownInterval
            this.mRemainingTime = millisInFuture
        }

        override fun onTick(millisUntilFinished: Long) {
            this.mRemainingTime = millisUntilFinished
        }

        override fun onFinish() {
        }

        fun pause() {
            this.cancel()
        }

        fun resume(): MyCountDownTimer {
            val myCountDownTimer = MyCountDownTimer(mRemainingTime, mInterval)
            myCountDownTimer.start()
            return myCountDownTimer
        }

    }
}