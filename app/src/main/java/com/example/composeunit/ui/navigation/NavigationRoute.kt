package com.example.composeunit.ui.navigation;

import androidx.core.os.bundleOf
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Created by wangfei44 on 2022/11/20.
 */
object NavigationRoute {
    const val SPLASH_PAGE_ROUTE = "SplashPage"
    const val LOGIN_PAGE_ROUTE = "LoginPage"
    const val HOME_PAGE_ROUTE = "com.example.composeunit.project.HomePage"
    const val SETTING_PAGE_ROUTE = "SettingPage"
    const val MESSAGE_DETAILS_PAGE_ROUTE = "message_details"
    const val settingOpenAIRoute = "com.example.composeunit.project.HomePage/ThreeFragment/OpenAIPage"

}
