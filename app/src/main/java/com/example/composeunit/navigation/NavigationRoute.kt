package com.example.composeunit.navigation;

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
}

// 路由跳转
fun navigatorTo(
    navCtrl: NavHostController, route: String
) {
    navCtrl.navigate(route) {
        popUpTo(navCtrl.graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
    }
}
