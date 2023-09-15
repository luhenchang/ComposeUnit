package com.example.composeunit.ui.compose.custom

import android.util.Log
import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.composeunit.ui.theme.openAiLight
import kotlinx.coroutines.launch
import kotlin.math.abs

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableTabRowSimple() {
    val scope = rememberCoroutineScope()
    val titles = remember {
        mutableStateListOf("掘金小册", "字节内部课")
    }
    // Create a ViewPager state
    val pagerState = rememberPagerState(pageCount = { titles.size })
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            selectedTabIndex = pagerState.currentPage,
            contentColor = Color.Black,
            edgePadding = 10.dp,
            divider = {},
            indicator = {
            }
        ) {
            titles.forEachIndexed { index, data ->
                val selected = pagerState.currentPage == index
                Box(
                    Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                //Tab被点击后让Pager中内容动画形式滑动到目标页
                                pagerState.scrollToPage(index, 0f)
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data,
                        fontSize = if (selected) 18.sp else 16.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight(),
            pageNestedScrollConnection = object : NestedScrollConnection {
                fun Velocity.consumeOffsetOnOrientations(orientation: Orientation): Velocity {
                    return if (orientation == Orientation.Vertical) {
                        copy(x = 0f)
                    } else {
                        copy(y = 0f)
                    }
                }

                fun Offset.consumeVelocityOnOrientations(orientation: Orientation): Offset {
                    return if (orientation == Orientation.Vertical) {
                        copy(x = 0f)
                    } else {
                        copy(y = 0f)
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    return when (source) {
                        NestedScrollSource.Fling -> available.consumeVelocityOnOrientations(Orientation.Horizontal)
                        else -> Offset.Zero
                    }
                }

                override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                    return available.consumeOffsetOnOrientations(Orientation.Horizontal)
                }
            }
        ) { pagePosition ->
            SecondPager(pagerState)
        }


    }


}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SecondPager(stateOne: PagerState) {
    val data = remember {
        mutableStateListOf("Android", "IOS", "人工智能", "开发人员", "代码人生", "阅读", "购买")
    }
    val pagerState = rememberPagerState(pageCount = { data.size })
    val scope = rememberCoroutineScope()
    Column {
        ScrollableTabRow(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            selectedTabIndex = pagerState.currentPage,//展示的页码，和Pager的保持一致
            contentColor = Color.Black,
            edgePadding = 10.dp,
            divider = {},
            minTabWidth = 76.dp,
            indicator = { tabPositions ->
                if (tabPositions.isNotEmpty()) {
                    PagerTabIndicator(
                        tabPositions = tabPositions,
                        pagerState = pagerState,
                        paddingIndicatorWidth = 35.dp
                    )
                }
            }
        ) {
            data.forEachIndexed { index, data ->
                val selected = pagerState.currentPage == index
                Box(
                    Modifier
                        .height(40.dp)
                        .wrapContentWidth()
                        .clickable {
                            scope.launch {
                                pagerState.scrollToPage(index, 0f)//Tab被点击后让Pager中内容动画形式滑动到目标页
                            }
                        }, contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = data, fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }
        }
//        val draggableState = rememberDraggablePagerState(stateOne, pagerState)
//        draggableState.initUserScrollEnableType()
        HorizontalPager(
           // userScrollEnabled = draggableState.userScrollEnabled(),
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .draggable(
                    state = rememberDraggableState { onDetail ->
                        scope.launch {
                            //draggableState.setDraggableOnDetailToScrollToPage(onDetail)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    //enabled = (draggableState.draggableEnabled())
                ),
            pageNestedScrollConnection = object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    Log.e("tag two", "consumed=$consumed")
                    return when (source) {
                        else -> Offset.Zero
                    }
                }

                override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                    return consumed
                }
            }
        ) { pagePosition ->
            Log.e("tag", "加载页码$pagePosition")
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    items(32, key = { index ->
                        index
                    }) { index ->
                        if (index % 2 == 0)
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color(52, 54, 65, 255))
                                    .padding(top = 20.dp)
                            ) {
                                Text(
                                    text = "我是假数据，用户数据",
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                                )
                            }
                        else
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(openAiLight)
                                    .padding(top = 20.dp)
                            ) {
                                Text(
                                    text = "我是假数据，OpenAI返回数据，如果回答有问题，我希望你可以给我一些建议，我继续生成，文字不够两行。",
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                                )
                            }
                    }
                }
            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTabIndicator(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
    color: Color = MaterialTheme.colorScheme.primary,
    @FloatRange(from = 0.0, to = 1.0) percent: Float = 0.6f,
    height: Dp = 2.dp,
    paddingIndicatorWidth: Dp = 0.dp,
) {
    val currentPage by rememberUpdatedState(newValue = pagerState.currentPage)
    val fraction by rememberUpdatedState(newValue = pagerState.currentPageOffsetFraction)
    val currentTab = tabPositions[currentPage]
    val previousTab = tabPositions.getOrNull(currentPage - 1)
    val nextTab = tabPositions.getOrNull(currentPage + 1)
    Canvas(
        modifier = Modifier.width(50.dp),
        onDraw = {
            val indicatorWidth = currentTab.width.toPx() * percent
            val indicatorOffset = if (fraction > 0 && nextTab != null) {
                lerp(currentTab.left, nextTab.left, fraction).toPx()
            } else if (fraction < 0 && previousTab != null) {
                lerp(currentTab.left, previousTab.left, -fraction).toPx()
            } else {
                currentTab.left.toPx()
            }

            val canvasHeight = size.height
            val padding =
                minOf(paddingIndicatorWidth.toPx(), indicatorWidth + indicatorWidth * abs(fraction))
            drawRoundRect(
                color = color,
                topLeft = Offset(
                    indicatorOffset + (currentTab.width.toPx() * (1 - percent) / 2) + padding,
                    canvasHeight - height.toPx()
                ),
                size = Size(
                    indicatorWidth + indicatorWidth * abs(fraction) - padding * 2,
                    height.toPx()
                ),
                cornerRadius = CornerRadius(50f)
            )
        }
    )
}

