package com.example.composeunit.project.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.composeunit.R

/**
 * Created by wangfei44 on 2021/11/16.
 */
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
            mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
