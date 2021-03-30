package com.example.myfirstcomposeapp.confing
import HomePage
import LoginPage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.myfirstcomposeapp.confing.Constant.HOME_PAGE_ROUTE
import com.example.myfirstcomposeapp.confing.Constant.LOGIN_PAGE_ROUTE
import com.example.myfirstcomposeapp.confing.Constant.MESSAGE_DETAILS_PAGE_ROUTE
import com.example.myfirstcomposeapp.project.fragment.MessageDetailsPage

object Constant {
    const val LOGIN_PAGE_ROUTE = "loginPage"
    const val HOME_PAGE_ROUTE = "homePage"
    const val MESSAGE_DETAILS_PAGE_ROUTE = "message_details"

}

@InternalComposeApi
@Composable
fun NavGraph(startDestination: String = LOGIN_PAGE_ROUTE) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    //设置导航
    NavHost(
        navController = navController,
        startDestination = startDestination,

    ) {
        composable(LOGIN_PAGE_ROUTE) {
            LoginPage(mainActions = actions)
        }

        composable(HOME_PAGE_ROUTE) {
            HomePage(actions)
        }
        composable(MESSAGE_DETAILS_PAGE_ROUTE) {
            MessageDetailsPage(actions)
        }


    }
}

class MainActions(navController: NavController) {
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
}
