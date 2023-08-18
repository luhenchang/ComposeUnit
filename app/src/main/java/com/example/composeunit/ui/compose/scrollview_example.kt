package com.example.composeunit.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeunit.R
import kotlin.math.roundToInt

/**
 * Created by wangfei44 on 2023/3/24.
 */
@Composable
fun NestedScrollViewCompose() {
    val imageHeight = 150.dp
    val headerHeight = 200.dp
    val topBarHeight = 48.dp
    val state = rememberLazyListState()
    val headerOffsetHeightPx = remember {
        mutableStateOf(0f)
    }
    val headerHeightPx = with(LocalDensity.current) {
        imageHeight.roundToPx().toFloat()
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 说明是向下滚,需要判断list是否已经滚动完了，滚动完了才去滚动
                if (available.y > 0) {
                    if (state.firstVisibleItemIndex <= 2) {
                        val delta = available.y
                        val newOffset = headerOffsetHeightPx.value + delta
                        headerOffsetHeightPx.value = newOffset.coerceIn(-headerHeightPx, 0f)
                    }
                } else {
                    val delta = available.y
                    val newOffset = headerOffsetHeightPx.value + delta
                    headerOffsetHeightPx.value = newOffset.coerceIn(-headerHeightPx, 0f)
                }
                return Offset.Zero
            }
        }
    }
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    Box(modifier = Modifier.nestedScroll(nestedScrollConnection, nestedScrollDispatcher)) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(top = headerHeight + topBarHeight)
        ) {
            items(100) { index ->
                Text(
                    "I'm item $index", modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        headerView(headerOffsetHeightPx.value.roundToInt())
        TopAppBar(
            modifier = Modifier
                .height(48.dp)
                .background(Color.Blue), contentPadding = PaddingValues(start = 20.dp)
        ) {
            Text(text = "标题", fontSize = 17.sp)
        }
    }
}

@Composable
fun headerView(headerOffsetY: Int) {
    Column(modifier = Modifier
        .padding(top = 48.dp)
        .offset {
            IntOffset(x = 0, y = headerOffsetY)
        }) {
        // 一张图片。高度是150dp
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp),
            bitmap = ImageBitmap.imageResource(id = R.drawable.shared_icon),
            contentDescription = "图片",
            contentScale = ContentScale.FillBounds
        )
        // 一个TabRow，高度是50dp
        tabRowView()
    }
}

@Composable
fun tabRowView() {
    val tabIndex = remember {
        mutableStateOf(0)
    }
    val tabDatas = ArrayList<String>().apply {
        add("语文")
        add("数学")
        add("英语")
    }
    TabRow(
        selectedTabIndex = tabIndex.value,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        backgroundColor = Color.Green,
        contentColor = Color.Black,
        divider = {
            TabRowDefaults.Divider()
        },
        indicator = {
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(it[tabIndex.value]),
                color = Color.Blue,
                height = 2.dp
            )
        }
    ) {
        tabDatas.forEachIndexed { index, s ->
            tabView(index, s, tabIndex)
        }
    }
}

@Composable
fun tabView(index: Int, text: String, tabIndex: MutableState<Int>) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPress = interactionSource.collectIsPressedAsState().value
    Tab(
        selected = index == tabIndex.value,
        onClick = {
            tabIndex.value = index
        },
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight(),
        enabled = true,
        interactionSource = interactionSource,
        selectedContentColor = Color.Red,
        unselectedContentColor = Color.Black
    ) {
        Text(
            text = text,
            color = if (isPress || index == tabIndex.value) Color.Red else Color.Black
        )
    }
}