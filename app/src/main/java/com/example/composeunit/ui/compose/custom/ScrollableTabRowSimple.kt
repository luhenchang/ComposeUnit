package com.example.composeunit.ui.compose.custom

import android.util.Log
import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableTabRowSimple() {
    val scope = rememberCoroutineScope()
    val titles = remember {
        mutableStateListOf("Compose", "Xml")
    }
    // Create a ViewPager state
    val pagerState = rememberPagerState(pageCount = { titles.size })
    Column {
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
                        fontSize = 16.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
        ) { pagePosition ->
            when (pagePosition) {
                0 -> {
                    Box(
                        Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Second(pagerState)
                    }
                }

                1 -> {
                    Second(pagerState)
                }

                2 -> {
                    Second(pagerState)
                }
            }
        }


    }


}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun Second(stateOne: PagerState) {
    val data = remember {
        mutableStateListOf("Text", "Box", "Row", "Column", "Space")
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
        val draggableState = rememberDraggablePagerState(stateOne, pagerState)
        draggableState.initUserScrollEnableType()
        HorizontalPager(
            userScrollEnabled = draggableState.userScrollEnabled(),
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .draggable(
                    state = rememberDraggableState { onDetail ->
                        scope.launch {
                            draggableState.setDraggableOnDetailToScrollToPage(onDetail)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    enabled = (draggableState.draggableEnabled())
                )
        ) { pagePosition ->
            Log.e("tag", "加载页码$pagePosition")
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "页面：：=${pagePosition}")
            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTabIndicator(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
    color: Color = Color.Green,
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

