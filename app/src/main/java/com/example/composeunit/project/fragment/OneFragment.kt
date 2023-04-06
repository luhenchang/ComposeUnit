package com.example.composeunit.project.fragment

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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
import com.example.composeunit.composeble_ui.home.getColorEndForIndex
import com.example.composeunit.composeble_ui.home.getColorForIndex
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

    ConstraintLayout {
        val (bottomListView, topTab) = createRefs()
        if (state.isEmpty()) {
            LoadingPageUI(tabSelectedState.value)
        } else {
            LazyColumn(
                state = scrollLazyState,
                contentPadding = PaddingValues(top = 125.dp),
                modifier = Modifier
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
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .constrainAs(topTab) {
                }
        ) {
            for (index in 0..6) {
                ComposeTabView(
                    "TV",
                    modifier = Modifier.weight(1f),
                    index = index,
                    tabSelectedState = tabSelectedState,
                    scaleH = animalTabHeightScale,
                    homeViewModel
                )
            }
        }
    }

}

@Composable
fun LoadingPageUI(index: Int) {
    val waveWidth=30f
    val waveHeight=21f
    val animalValue by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(170.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            drawIntoCanvas{
                val  canvas:android.graphics.Canvas = it.nativeCanvas
                canvas.scale(1f, -1f)

                val roundRect = Path()
                roundRect.addRoundRect(waveWidth * 4,waveWidth*3*(1-animalValue),waveWidth* 8,-waveWidth*3,60f,60f,Path.Direction.CCW)
                canvas.clipPath(roundRect)

                canvas.translate(animalValue*(waveWidth*4), 0f) //内层海浪
                val wavePath= Path()
                wavePath.moveTo(0f, -waveWidth * 6)
                wavePath.lineTo(0f, 0f)
                wavePath.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
                wavePath.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
                wavePath.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
                wavePath.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
                wavePath.lineTo(waveWidth * 8, -waveWidth * 6)
                canvas.drawPath(wavePath, getPaintBefore(Paint.Style.FILL,waveWidth, index))

                canvas.translate(animalValue*(waveWidth*4), 0f) //内层海浪
                val wavePathOut= Path()
                wavePathOut.moveTo(-waveWidth * 7, -waveWidth * 6)
                wavePathOut.lineTo(-waveWidth * 7, 0f)
                wavePathOut.quadTo(-waveWidth * 7, waveHeight, -waveWidth * 6, 0f)
                wavePathOut.quadTo(-waveWidth * 5, -waveHeight, -waveWidth * 4, 0f)
                wavePathOut.quadTo(-waveWidth * 3, waveHeight, -waveWidth * 2, 0f)
                wavePathOut.quadTo(-waveWidth, -waveHeight, 0f, 0f)
                wavePathOut.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
                wavePathOut.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
                wavePathOut.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
                wavePathOut.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
                wavePathOut.lineTo(waveWidth * 8, -waveWidth * 6)
                canvas.drawPath(wavePathOut, getPaint(Paint.Style.FILL,waveWidth,index))
            }
        }
    }
}
fun getPaintBefore(style: Paint.Style, waveWidth: Float, index :Int): Paint {
    val gPaint = Paint()
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = getColorForIndex(index).toArgb()
    val linearGradient = LinearGradient(
        waveWidth * 4, -waveWidth * 8,
        waveWidth * 4,
        80f,
        getColorForIndex(index).toArgb(),
        getColorEndForIndex(index).toArgb(),
        Shader.TileMode.CLAMP
    )
    gPaint.shader=linearGradient
    return gPaint
}

private fun getPaint(style: Paint.Style,waveWidth:Float,index:Int): Paint {
    val gPaint = Paint()
    gPaint.color = android.graphics.Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = android.graphics.Color.argb(255, 75, 151, 79)
    val linearGradient = LinearGradient(
        waveWidth * 4, -waveWidth * 2,
        waveWidth * 4,
        80f,
        getColorForIndex(index).toArgb(),
        getColorEndForIndex(index).toArgb(),
        Shader.TileMode.CLAMP
    )
    gPaint.shader=linearGradient
    return gPaint
}

