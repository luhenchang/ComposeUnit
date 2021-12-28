package com.example.composeunit.project.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.composeunit.project.service.NotificationCompatHelper
import com.example.composeunit.project.service.RecorderService
import com.example.composeunit.project.service.SystemUtils

import android.widget.RemoteViews
import com.example.composeunit.R
import android.content.ComponentName
import android.util.Log


/**
 * Created by wangfei44 on 2021/12/28.
 */
class UpdateRecorderWidgetService : Service() {
    var TAG =UpdateRecorderWidgetService.javaClass.name
    companion object {
        const val NOTIFICATION_ID = Int.MAX_VALUE - 1
        const val ACTION_NAME = "action_type"
        const val ACTION_INVALID = 0
        const val ACTION_RECORD = 1

    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG,"onCreate")
        if (SystemUtils.isOOrLater()) {
            startForeground(
                NOTIFICATION_ID,
                NotificationCompatHelper.getInstance(applicationContext)?.getBuilder()?.build()
            )
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle == null || !bundle.containsKey(RecorderService.ACTION_NAME))
            return super.onStartCommand(intent, flags, startId)
        Log.i("onStartCommand::bundle=",bundle.getInt(ACTION_NAME, ACTION_INVALID).toString())
        when (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
            ACTION_RECORD -> {//开始录制更新小部件
                val componentName = ComponentName(
                    this,
                    RecorderAppWidget::class.java
                )
                val remoteViews = RemoteViews(packageName, R.layout.widget_recorder_small)
                remoteViews.setTextViewText(R.id.widget_title_text, "开始录制")
                remoteViews.setTextViewText(R.id.widget_start_bn, "我开始了")
                remoteViews.setTextViewText(R.id.widget_stop_bn, "我暂停了")

                //由AppwidgetManager 处理更新widget
                var awm = AppWidgetManager.getInstance(applicationContext)
                awm.updateAppWidget(componentName,remoteViews)
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return RecorderBind()
    }

    class RecorderBind : Binder() {
        val service: UpdateRecorderWidgetService get() = UpdateRecorderWidgetService()
    }

}