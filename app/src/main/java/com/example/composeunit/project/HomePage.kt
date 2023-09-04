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
import com.example.composeunit.ui.compose.home.BottomNavigation
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
            BottomNavigation(
                homeViewModel,
                onTapBottom = {
                    actionsMain::navigateTo.invoke(it)
                }, Modifier
            )
        }
    ) {
        BottomNavGraph(it, navCtrl = navCtrl, viewModel, actionsMain, mainActions)
    }
}

@Composable
fun BottomNavGraph(
    innerPaddingValues: PaddingValues? = null,
    navCtrl: NavHostController,
    viewModel: SplashViewModel,
    actionsMain: NavigationActions,
    mainActions: MainActions
) {
    NavHost(
        navController = navCtrl,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(splashViewModel = viewModel, onBack = {
                actionsMain.navigateTo(BottomBarScreen.Widget.sendArgumentRoute(it))
            })
        }
        composable(
            BottomBarScreen.Widget.argumentRoute(),
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { entry ->
            val vieModel: MessageViewModel = viewModel()
            val index = entry.arguments?.getInt("index") ?: 0
            WidgetScreen(mainActions, vieModel, index = index)
        }
        composable(BottomBarScreen.Setting.route) {
            SettingScreen(mainActions)
        }
    }
}

