import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeunit.confing.MainActions
import com.example.composeunit.project.fragment.TwoFragment
import com.example.composeunit.project.view_model.home.HomeViewModel
import  com.example.composeunit.composeble_ui.home.*
import com.example.composeunit.navigation.NavigationRoute
import com.example.composeunit.navigation.navigatorTo
import com.example.composeunit.project.fragment.OneFragment
import com.example.composeunit.project.fragment.ThreeFragment
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.example.composeunit.project.view_model.message.MessageViewModel

@Composable
fun HomePage(
    mainActions: MainActions,
    homeViewModel: HomeViewModel = viewModel(),
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
                OneFragment()
            }
            composable(NavigationRoute.widgetRoute) {
                val vieModel: MessageViewModel = viewModel()
                TwoFragment(mainActions, vieModel)
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


