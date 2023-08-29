import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composeunit.project.fragment.OneFragment
import com.example.composeunit.project.fragment.ThreeFragment
import com.example.composeunit.project.fragment.TwoFragment
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.project.view_model.message.MessageViewModel
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.confing.MainActions
import com.example.composeunit.ui.compose.home.BottomNavigation
import com.example.composeunit.ui.compose.navigation.NavigationRoute

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
                    actionsMain.navigateTo(it)
                }, Modifier
            )
        }
    ) {
        BottomNavGraph(navCtrl = navCtrl,viewModel,actionsMain,mainActions)
        Log.e("it", it.toString())
    }
}

@Composable
fun BottomNavGraph(
    navCtrl: NavHostController,
    viewModel: SplashViewModel,
    actionsMain: NavigationActions,
    mainActions: MainActions
) {
    NavHost(
        navCtrl,
        startDestination = NavigationRoute.homeRoute,
    ) {
        composable(NavigationRoute.homeRoute) {
            OneFragment(splashViewModel = viewModel, onBack = {
                actionsMain.navigateTo(NavigationRoute.widgetRoute + "/$it")
            })
        }
        composable(
            NavigationRoute.widgetRoute + "/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { entry ->
            val vieModel: MessageViewModel = viewModel()
            val index = entry.arguments?.getInt("index") ?: 0
            TwoFragment(mainActions, vieModel, index = index)
        }
        composable(NavigationRoute.settingRoute) {
            ThreeFragment(mainActions)
        }
    }
}

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
