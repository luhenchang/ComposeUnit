package com.example.composeunit.ui.compose.home.widget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composeunit.R
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.home.HomeItemView
import com.example.composeunit.ui.compose.home.LoadingPageUI

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
        mutableIntStateOf(0)
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
                        .fillMaxWidth().alpha(0.5f)
                ) {
                    HomeItemView(state[0], index, homeViewModel)
                }
            }
        }
    }
}