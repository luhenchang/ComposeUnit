import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstcomposeapp.confing.MainActions
import com.example.myfirstcomposeapp.project.fragment.OneFragment
import com.example.myfirstcomposeapp.project.fragment.TwoFragment
import com.example.myfirstcomposeapp.project.model.home.HomeViewModel
import  com.example.myfirstcomposeapp.composeble_ui.home.*
import com.example.myfirstcomposeapp.confing.MyTopAppBar
import com.example.myfirstcomposeapp.project.fragment.OneFragment1
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun HomePage(mainActions: MainActions, homeViewModel: HomeViewModel = viewModel()) {
    //开始观察此[LiveData]，并通过[State]表示其值。每次将新值发布到[LiveData]中时，返回的[State]将被更新，
    val position by homeViewModel.position.observeAsState()
    Scaffold(
        Modifier.navigationBarsPadding(false),
        topBar = {MyTopAppBar(mainActions,position)},
        bottomBar = {
          Column {
              BottomNavigation(homeViewModel)
              Spacer(modifier = Modifier.navigationBarsHeight())
          }
        }) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (position) {
            0 -> OneFragment1(modifier)
            1 -> TwoFragment(mainActions)
            else -> TwoFragment(mainActions)
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


