package com.example.composeunit.ui.compose.other.service

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.composeunit.MainActivity
import com.example.composeunit.R

/**
 * Created by wangfei44 on 2021/11/16.
 */
fun showNotification(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val popDialogIntent = Intent(context, MainActivity.PopBroadCastReceiver::class.java).apply {
        action = "com.example.composeunit.pop"
        putExtra(EXTRA_NOTIFICATION_ID, 0)
    }
    val pendIntent =
        PendingIntent.getBroadcast(context, 0, popDialogIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val notificationBuilder =
        NotificationCompat.Builder(context, RecorderService.CHANNEL_ID)
            .setSmallIcon(R.drawable.delected_icon)
            .setContentTitle("HIGH PRIORITY")
            .setContentText("Check this dog puppy video NOW!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendIntent,true)
            .setCategory(NotificationCompat.CATEGORY_SYSTEM)
    notificationManager.notify(666, notificationBuilder.build())
}

class NotificationCompatHelper(context: Context) {
    private var mChannel: NotificationChannel? = null
    private val mContext: Context? = context.applicationContext
    private var mNotificationManager: NotificationManager? = null
    fun updateChannelName() {
        if (SystemUtils.isOOrLater()) {
            val channelName = mContext!!.resources.getString(R.string.notification_channel_name)
            mChannel!!.name = channelName
            getManager()!!.createNotificationChannel(mChannel!!)
        }
    }

    private fun getBuilder(isCustom: Boolean): Notification.Builder {
        val builder: Notification.Builder
        if (SystemUtils.isOOrLater()) {
            builder = Notification.Builder(mContext, DEFAULT_ID)
            if (isCustom) {
                builder.style = Notification.DecoratedCustomViewStyle()
            }
            builder.setColor(mContext!!.resources.getColor(R.color.notification_btn))
        } else {
            builder = Notification.Builder(mContext)
        }
        return builder
    }

    fun getBuilder(): Notification.Builder {
        return getBuilder(false)
    }

    fun getCustomBuilder(): Notification.Builder {
        return getBuilder(true)
    }

    private fun getManager(): NotificationManager? {
        if (mNotificationManager == null && mContext != null) {
            mNotificationManager =
                mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mNotificationManager
    }

    companion object {
        private const val TAG = "NotificationCompatHelper"
        private const val DEFAULT_ID = "com_zui_recorder_channel_id"
        var mInstance: NotificationCompatHelper? = null
        fun getInstance(context: Context): NotificationCompatHelper? {
            if (mInstance == null) {
                mInstance = NotificationCompatHelper(context)
            }
            return mInstance
        }
    }

    init {
        val channelName = mContext?.resources?.getString(R.string.notification_channel_name)
        if (SystemUtils.isOOrLater()) {
            mChannel = NotificationChannel(
                DEFAULT_ID,
                channelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel!!.setSound(null, null)
            if (getManager() != null) {
                getManager()!!.createNotificationChannel(mChannel!!)
            }
        }
    }
}
