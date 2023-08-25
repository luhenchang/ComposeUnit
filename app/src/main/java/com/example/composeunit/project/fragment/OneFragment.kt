package com.example.composeunit.project.fragment

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.R
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.home.ComposeTabView
import com.example.composeunit.ui.compose.home.HomeItemView
import com.example.composeunit.ui.compose.home.LoadingPageUI
import com.example.composeunit.utils.BitmapBlur
import com.example.composeunit.utils.getBitmap


@Composable
fun OneFragment(homeViewModel: HomeViewModel = viewModel(), splashViewModel:SplashViewModel,onBack:(index:Int)->Unit) {
    homeViewModel.getInformation(LocalContext.current)
    val tabSelectedState by homeViewModel.tabSelectedIndex.collectAsState()
    var heightValue by remember {
        mutableStateOf(0f)
    }
    ConstraintLayout {
        val (bottomListView, topTab) = createRefs()
        LoadingOrLazyColumn(modifier = Modifier
            .constrainAs(bottomListView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, tabSelectedState, homeViewModel,
            scrollYOffset = {
                heightValue = it
            })
        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .constrainAs(topTab) {
                },
            heightValue,
            homeViewModel,
            tabSelectedCallBack = {
                homeViewModel.selectedTabIndex(it)
                splashViewModel.updateTheme(it)
                onBack.invoke(it)
            },tabSelectedState = tabSelectedState)
    }
}

@Composable
fun LoadingOrLazyColumn(
    modifier: Modifier,
    selectedIndex: Int,
    homeViewModel: HomeViewModel,
    scrollYOffset: (Float) -> Unit
) {
    val state = homeViewModel.itemUIState.collectAsState().value
    val scrollLazyState = rememberLazyListState()
    var itemIndex by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        snapshotFlow {
            scrollLazyState.firstVisibleItemIndex
        }.collect { index ->
            itemIndex = index
            Log.e("offset index =", index.toString())
        }
    }
    var availableY by remember {
        mutableStateOf(0f)
    }
    //获取滑动方向
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                availableY = available.y
                return Offset.Zero
            }
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow {
            scrollLazyState.firstVisibleItemScrollOffset
        }.collect { offset ->
            Log.e("offset down::", " offset=$offset、availableY = $availableY")
            if (availableY < 0) {//向上滚
                Log.e(
                    "offset up::",
                    " offset=$offset,and offset=${scrollLazyState.firstVisibleItemScrollOffset}、availableY = $availableY"
                )
                if (itemIndex == 0 && offset < 100) {
                    scrollYOffset.invoke(offset.toFloat())
                }
            }

            if (availableY > 0) {//向下滚
                Log.e("offset down::", " offset=$offset、availableY = $availableY")
                if (itemIndex == 0 && offset < 100) {
                    scrollYOffset.invoke(offset.toFloat())
                }
            }
        }
    }
    if (state.isEmpty()) {
        LoadingPageUI(index = selectedIndex)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                painter = painterResource(id = R.drawable.hbmr),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
        }
        LazyColumn(
            state = scrollLazyState,
            contentPadding = PaddingValues(top = 125.dp),
            modifier = modifier.nestedScroll(nestedScrollConnection, null)
        ) {
            //遍历循环内部Item部件
            items(30) { index ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    HomeItemView(state[0], index, homeViewModel)
                }
            }
        }
    }
}

@Composable
private fun TabRow(
    modifier: Modifier = Modifier,
    heightValue: Float,
    homeViewModel: HomeViewModel,
    tabSelectedCallBack: (Int) -> Unit,
    tabSelectedState:Int
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
    ) {
        val title =
            arrayListOf("View", "ViewGp", "Scroll", "Canvas", "OpenAi", "Animal", "End..")
        for (index in 0..6) {
            ComposeTabView(
                title[index],
                modifier = Modifier.weight(1f),
                index = index,
                tabSelectedCallBack = tabSelectedCallBack::invoke,
                heightValue = heightValue,
                tabSelectedState,
                homeViewModel
            )
        }
    }
}





