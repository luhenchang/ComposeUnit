import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeunit.confing.MainActions
import com.example.composeunit.project.fragment.OneFragment
import com.example.composeunit.project.fragment.TwoFragment
import com.example.composeunit.project.view_model.home.HomeViewModel
import  com.example.composeunit.composeble_ui.home.*
import com.example.composeunit.confing.MyTopAppBar
import com.example.composeunit.navigation.NavigationRoute
import com.example.composeunit.navigation.navigatorTo
import com.example.composeunit.project.fragment.OneFragment1
import com.example.composeunit.project.fragment.ThreeFragment
import com.example.composeunit.project.view_model.message.MessageViewModel
import com.google.android.material.navigation.NavigationBarView

@Composable
fun HomePage(
    mainActions: MainActions,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val navCtrl = rememberNavController()
    val position by homeViewModel.position.observeAsState()
    Scaffold(
        Modifier.navigationBarsPadding(),
        topBar = { MyTopAppBar(mainActions, position) },
        backgroundColor = Color(236, 238, 240, 255),
        bottomBar = {
            BottomNavigation(homeViewModel,
                onTapBottom = {
                    navigatorTo(navCtrl, it)
                })
        }) { innerPadding ->
        NavHost(
            navCtrl,
            startDestination = NavigationRoute.homeRoute,
            Modifier.padding(innerPadding)
        ) {
            composable(NavigationRoute.homeRoute) {
                Log.e("OneFragment1 update", "update is more and more")
                OneFragment1()
            }
            composable(NavigationRoute.widgetRoute) {
                Log.e("TwoFragment  update", "update is more and more")
                val vieModel: MessageViewModel = viewModel()
                TwoFragment(mainActions, vieModel)
            }
            composable(NavigationRoute.settingRoute) {
                Log.e("ThreeFragment  update", "update is more and more")
                ThreeFragment(mainActions)
            }
        }
    }
}


/**
 * @param
 * 顶部appBar
 * @indexPageState 索引如果是第一个framgnet页面那就显示,其他的就不显示
 */
@Composable
fun AppBar(indexPageState: Int?) {
    if (indexPageState == 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFF108888)),
        ) {
            OneFragment(modifier = null)
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "Hello World", color = Color.White
            )
        }
    }
}


