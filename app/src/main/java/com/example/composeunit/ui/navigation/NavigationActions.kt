package com.example.composeunit.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.composeunit.R


class NavigationActions(private val navController: NavHostController) {
    fun navigateTo(route: String) {
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

object BottomNaviRoute {
    const val HOME = "HomeScreen"
    const val WIDGET = "WidgetScreen"
    const val SETTING = "SettingScreen"
}

sealed class BottomBarScreen(
    val route: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val title: Int
) {
    object Home : BottomBarScreen(
        route = BottomNaviRoute.HOME,
        R.drawable.home,
        R.drawable.home,
        R.string.home_name
    )

    object Widget : BottomBarScreen(
        route = BottomNaviRoute.WIDGET,
        R.drawable.center,
        R.drawable.center,
        R.string.home_widget
    ) {
        fun argumentRoute(): String {
            return "$route/{index}"
        }

        fun sendArgumentRoute(index: Int): String {
            return "$route/$index"
        }
    }

    object Setting : BottomBarScreen(
        route = BottomNaviRoute.SETTING,
        R.drawable.center,
        R.drawable.center,
        R.string.home_widget
    )

}

val BottomBarScreens = arrayListOf(
    BottomBarScreen.Home, BottomBarScreen.Widget, BottomBarScreen.Setting
)
