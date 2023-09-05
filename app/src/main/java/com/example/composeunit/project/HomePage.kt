package com.example.composeunit.project

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composeunit.project.fragment.HomeScreen
import com.example.composeunit.project.fragment.SettingScreen
import com.example.composeunit.project.fragment.WidgetScreen
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.project.view_model.message.MessageViewModel
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.confing.MainActions
import com.example.composeunit.ui.compose.home.BottomBarNavigation
import com.example.composeunit.ui.navigation.BottomBarScreen
import com.example.composeunit.ui.navigation.NavigationActions

@Composable
fun HomePage(
    mainActions: MainActions,
    homeViewModel: HomeViewModel = viewModel(),
    viewModel: SplashViewModel
) {
    val navCtrl = rememberNavController()
    val actionsMain = remember(navCtrl) { NavigationActions(navCtrl) }
    Scaffold(
        bottomBar = {
            BottomBarNavigation(
                homeViewModel,
                onTapBottom = {
                    actionsMain::navigateTo.invoke(it)
                }, Modifier,
                navController = navCtrl
            )
        }
    ) {
        BottomNavGraph(it, navCtrl = navCtrl, viewModel,homeViewModel, actionsMain, mainActions)
    }
}

@Composable
fun BottomNavGraph(
    innerPaddingValues: PaddingValues? = null,
    navCtrl: NavHostController,
    viewModel: SplashViewModel,
    homeViewModel: HomeViewModel,
    actionsMain: NavigationActions,
    mainActions: MainActions
) {
    //如果设置了innerPaddingValues
    Log.e("innerPaddingValues==", "$innerPaddingValues")
    NavHost(
        navController = navCtrl,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(splashViewModel = viewModel, onBack = {
                homeViewModel.positionChanged(1)
                actionsMain.navigateTo(BottomBarScreen.Widget.sendArgumentRoute(it))
            })
        }
        composable(
            BottomBarScreen.Widget.argumentRoute(),
            arguments = listOf(navArgument(BottomBarScreen.Widget.INDEX) { type = NavType.IntType })
        ) { entry ->
            val vieModel: MessageViewModel = viewModel()
            val index = entry.arguments?.getInt(BottomBarScreen.Widget.INDEX) ?: 0
            WidgetScreen(mainActions, vieModel, index = index)
        }
        composable(BottomBarScreen.Setting.route) {
            SettingScreen(mainActions)
        }
    }
}

