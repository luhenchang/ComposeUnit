import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeunit.ui.compose.confing.MainActions
import com.example.composeunit.project.fragment.TwoFragment
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.navigation.NavigationRoute
import com.example.composeunit.ui.compose.navigation.navigatorTo
import com.example.composeunit.project.fragment.OneFragment
import com.example.composeunit.project.fragment.ThreeFragment
import com.example.composeunit.project.view_model.message.MessageViewModel
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.home.BottomNavigation
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun HomePage(
    mainActions: MainActions,
    homeViewModel: HomeViewModel = viewModel(),
    viewModel:SplashViewModel
) {
    val navCtrl = rememberNavController()
    Box(
        Modifier.fillMaxSize()
    ) {
        NavHost(
            navCtrl,
            startDestination = NavigationRoute.homeRoute,
        ) {
            composable(NavigationRoute.homeRoute) {
                OneFragment(splashViewModel = viewModel, onBack ={
                    navigatorTo(navCtrl, NavigationRoute.widgetRoute+"/$it")
                })
            }
            composable(NavigationRoute.widgetRoute+"/{index}", arguments = listOf(navArgument("index") { type = NavType.IntType })){entry->
                val vieModel: MessageViewModel = viewModel()
                val index = entry.arguments?.getInt("index")?:0
                TwoFragment(mainActions, vieModel,index = index)
            }
            composable(NavigationRoute.settingRoute) {
                ThreeFragment(mainActions)
            }
        }
        BottomNavigation(
            homeViewModel,
            onTapBottom = {
                navigatorTo(navCtrl, it)
            }, Modifier.align(Alignment.BottomCenter)
        )
    }
}


