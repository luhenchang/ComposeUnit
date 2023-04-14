package com.example.composeunit.confing
import HomePage
import LoginPage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeunit.navigation.NavigationRoute.HOME_PAGE_ROUTE
import com.example.composeunit.navigation.NavigationRoute.LOGIN_PAGE_ROUTE
import com.example.composeunit.navigation.NavigationRoute.MESSAGE_DETAILS_PAGE_ROUTE
import com.example.composeunit.navigation.NavigationRoute.SETTING_PAGE_ROUTE
import com.example.composeunit.navigation.NavigationRoute.SPLASH_PAGE_ROUTE
import com.example.composeunit.navigation.NavigationRoute.settingOpenAIRoute
import com.example.composeunit.project.SplashCompass
import com.example.composeunit.project.fragment.MessageDetailPage
import com.example.composeunit.project.fragment.ThreeFragment
import com.example.composeunit.project.page.OpenAIPage
import com.example.composeunit.project.view_model.ai.OpenAiViewModel

@Composable
fun NavGraph(startDestination: String = settingOpenAIRoute) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    //设置导航
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(SPLASH_PAGE_ROUTE){
                SplashCompass(actions)
            }
            composable(LOGIN_PAGE_ROUTE) {
                LoginPage(actions,navController)
            }
            composable(HOME_PAGE_ROUTE) {
                HomePage(actions)
            }
            composable(SETTING_PAGE_ROUTE){
                ThreeFragment(actions)
            }
            composable(MESSAGE_DETAILS_PAGE_ROUTE) {
                MessageDetailPage(actions)
            }
            composable(settingOpenAIRoute) {
                val viewModel: OpenAiViewModel = viewModel()
                OpenAIPage(viewModel)
            }
        }
    )
}

class MainActions(navController: NavController) {
    //启动页面
    val splashPage: () ->Unit = {
        navController.navigate(SPLASH_PAGE_ROUTE)
    }
    //主页面
    val homePage: () -> Unit = {
        navController.navigate(HOME_PAGE_ROUTE)
    }

    //登陆页面
    val loginPage: () -> Unit = {
        navController.navigate(LOGIN_PAGE_ROUTE)
    }

    //返回上一个堆栈
    val popCurrenPage: () -> Unit = {
        navController.navigateUp()
    }
    //中间信息页面的详情页面
    val messageDetailsPage:() -> Unit = {
        navController.navigate(MESSAGE_DETAILS_PAGE_ROUTE)
    }

    val openAIPage: () -> Unit = {
        navController.navigate(settingOpenAIRoute)
    }
}
