package com.example.composeunit.repository.datastore

import com.example.composeunit.R
import com.example.composeunit.models.SettingWidgetModel

/**
 * Created by wangfei44 on 2022/11/20.
 */
object SettingWidgetDataStore {
    val allItemWidget = listOf(
        SettingWidgetModel(0, "设置中心", R.drawable.android_icon),
        SettingWidgetModel(0, "我的收藏", R.drawable.android_icon),
        SettingWidgetModel(0, "版本信息", R.drawable.android_icon),
        SettingWidgetModel(0, "关于应用", R.drawable.android_icon)
    )
}