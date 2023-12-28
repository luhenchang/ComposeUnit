package com.example.composeunit.ui.compose.other.ai.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.composeunit.R
import com.example.composeunit.ui.compose.other.service.RecorderService

/**
 * Created by wangfei44 on 2021/12/28.
 */
class RecorderAppWidget : AppWidgetProvider() {
    var TAG = this.javaClass.name
    companion object{
        const val UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
    }
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i(TAG,"onUpdate")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    //当Widget第一次创建的时候，该方法调用，然后启动后台的服务
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.i(TAG,"onEnabled")
        val startIntent = Intent(context, UpdateRecorderWidgetService::class.java)
        context.startService(startIntent)
    }

    //当把桌面上的Widget全部都删掉的时候，调用该方法
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.i(TAG,"onDisabled")
        val stopUpdateIntent = Intent(context, UpdateRecorderWidgetService::class.java)
        context.stopService(stopUpdateIntent);
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.i(TAG,"onReceive")
        when(intent.action){
            UPDATE_ACTION ->{
                val time = intent.getLongExtra("time",0L)/1000
                Log.i(TAG,"更新小部件么?=${formatTime(time)}")
                val appWidgetIds = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(
                        ComponentName(
                            context,
                            RecorderAppWidget::class.java
                        )
                    )
                val remoteViews = RemoteViews(context.packageName, R.layout.widget_recorder_small)
                remoteViews.setTextViewText(R.id.widget_title_text, time.toString())
                remoteViews.setOnClickPendingIntent(R.id.widget_stop_bn, PendingIntent
                    .getBroadcast(
                        context, 0, Intent()
                            .setAction(RecorderService.ACTION_CANCEL_TIMER),
                        PendingIntent.FLAG_MUTABLE
                    ))

                remoteViews.setOnClickPendingIntent(R.id.widget_start_bn, PendingIntent
                    .getBroadcast(
                        context, 0, Intent()
                            .setAction(RecorderService.ACTION_RESUME_TIMER),
                        PendingIntent.FLAG_MUTABLE
                    ))

                remoteViews.setTextViewText(R.id.widget_start_bn, "我开始了")
                remoteViews.setTextViewText(R.id.widget_stop_bn, "我暂停了")
                //由AppwidgetManager 处理更新widget
                val awm = AppWidgetManager.getInstance(context.applicationContext)
                awm.updateAppWidget(appWidgetIds,remoteViews)
            }
        }

    }
    private fun formatTime(seconds: Long): String {
        return String.format(" %02d:%02d", seconds / 60, seconds % 60)
    }
}