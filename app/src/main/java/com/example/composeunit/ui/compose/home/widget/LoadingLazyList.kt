package com.example.composeunit.ui.compose.home.widget

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composeunit.R
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.home.HomeItemView
import com.example.composeunit.ui.compose.home.LoadingPageUI

@Composable
fun LoadingLazyList(
    modifier: Modifier,
    selectedIndex: Int,
    scrollState: HomeScrollState,
    homeViewModel: HomeViewModel
) {
    val state = homeViewModel.itemUIState.collectAsState().value
    val targetHeight = 20.dp
    val targetHeightPx = with(LocalDensity.current) { targetHeight.roundToPx().toFloat() }
    val listState = rememberLazyListState()
    scrollState.setListState(listState)
    val nestedScrollConnection = remember {
        HomeScrollConnection(homeScrollState = scrollState, targetHeightPx)
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
            state = listState,
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