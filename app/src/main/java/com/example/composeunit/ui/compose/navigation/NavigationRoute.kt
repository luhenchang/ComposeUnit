package com.example.composeunit.ui.compose.navigation;

import androidx.core.os.bundleOf
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Created by wangfei44 on 2022/11/20.
 */
object NavigationRoute {
    const val SPLASH_PAGE_ROUTE = "SplashPage"
    const val LOGIN_PAGE_ROUTE = "LoginPage"
    const val HOME_PAGE_ROUTE = "HomePage"
    const val SETTING_PAGE_ROUTE = "SettingPage"
    const val MESSAGE_DETAILS_PAGE_ROUTE = "message_details"
    const val homeRoute = "HomePage/OnFragment"
    const val widgetRoute = "HomePage/TwoFragment"
    const val settingRoute = "HomePage/ThreeFragment"
    const val settingOpenAIRoute = "HomePage/ThreeFragment/OpenAIPage"

}

// 路由跳转
fun navigatorTo(
    navCtrl: NavHostController, route: String
) {
    navCtrl.navigate(route) {
        // 避免 BackStack 增长，跳转页面时，将栈内 startDestination 之外的页面弹出
        popUpTo(navCtrl.graph.findStartDestination().id) {
            //出栈的 BackStack 保存状态
            saveState = true
        }
        // 避免点击同一个 Item 时反复入栈
        launchSingleTop = true
        // 如果之前出栈时保存状态了，那么重新入栈时恢复状态
        restoreState = true
    }
}
