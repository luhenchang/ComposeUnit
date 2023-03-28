package com.example.composeunit.project.fragment

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.composeble_ui.home.ComposeTabView
import com.example.composeunit.composeble_ui.home.HomeItemView
import com.example.composeunit.project.view_model.home.HomeViewModel


@Composable
fun OneFragment(homeViewModel: HomeViewModel = viewModel()) {
    homeViewModel.getInformation(LocalContext.current)
    //设置滑动
    val scrollLazyState = rememberLazyListState()
    val state = homeViewModel.itemUIState.collectAsState().value
    val tabSelectedState = remember { mutableStateOf(0) }

    var availableY by remember {
        mutableStateOf(0f)
    }

    //展开和收起控制点
    var tabOpenOrClose by remember {
        mutableStateOf(true)
    }
    //进行配置Tab动画
    val animalTabHeightScale: Float by animateFloatAsState(
        if (tabOpenOrClose) {
            1f
        } else {
            0.7f
        }, animationSpec = TweenSpec(durationMillis = if (tabOpenOrClose) 10 else 100)
    )
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                //向下滚,判断list是否已经滚动到第0个，滚动完了才去刷新顶部TabLayout
                availableY = available.y
                return Offset.Zero
            }
        }
    }

    //这里首先根据显示的最上面ItemView.Index来根据是目前是上滑还是下滑来进行驱动TabView的展开和半关闭。
    //因为要用动画所以转为Flow去减少重复点的上报导致不断重回性能问题，例如滑动到顶部继续上滑，还是会上报availableY的
    LaunchedEffect(Unit) {
        snapshotFlow {
            scrollLazyState.firstVisibleItemIndex
        }.collect { index ->
            if (availableY > 0) {//向下滚
                if (index < 1) {
                    tabOpenOrClose = true
                }
            } else {//向上滚
                if (index > 0) {
                    tabOpenOrClose = false
                }
            }
        }
    }
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    if (state.isNullOrEmpty()) {
        Text(text = "数据加载中")
    } else {
        Scaffold { innerPadding ->
            ConstraintLayout {
                val (bottomListView, topTab) = createRefs()
                LazyColumn(
                    state = scrollLazyState,
                    contentPadding = PaddingValues(top = 125.dp),
                    modifier = Modifier
                        .padding(innerPadding)
                        .constrainAs(bottomListView) {
                            top.linkTo(parent.top)  // 顶部约束为父布局的顶部
                            start.linkTo(parent.start)  // 左侧约束为父布局的左侧
                            end.linkTo(parent.end)  // 右侧约束为父布局的右侧
                        }
                        .nestedScroll(nestedScrollConnection, nestedScrollDispatcher)
                        .background(Color(238, 239, 247, 255))) {
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .constrainAs(topTab) {}
                ) {
                    for (index in 0..6) {
                        ComposeTabView(
                            modifier = Modifier.weight(1f),
                            index = index,
                            tabSelectedState = tabSelectedState,
                            scaleH = animalTabHeightScale
                        )
                    }
                }
            }
        }
    }
}
