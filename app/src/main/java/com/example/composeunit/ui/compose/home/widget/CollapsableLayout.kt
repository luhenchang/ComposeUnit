package com.example.composeunit.ui.compose.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.composeunit.ui.compose.home.HomeViewModel
import com.example.composeunit.ui.compose.SplashViewModel

@Composable
fun rememberHomeScrollState(scrollOffsetOfY: Float = 0f): HomeScrollState {
    val density = LocalDensity.current
    return remember(scrollOffsetOfY, density) {
        HomeScrollState(scrollOffsetOfY, density)
    }
}

@Composable
fun CollapsableLayout(
    homeViewModel: HomeViewModel,
    scrollState: HomeScrollState = rememberHomeScrollState(0f),
    tabSelectedState: Int,
    splashViewModel: SplashViewModel,
    onBack: (index: Int) -> Unit
) {
    ConstraintLayout {
        val (bottomListView, topTab) = createRefs()
        LoadingLazyList(modifier = Modifier
            .constrainAs(bottomListView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            tabSelectedState,
            homeViewModel = homeViewModel,
            scrollState = scrollState)
        HomeScreenTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .constrainAs(topTab) {
                },
            scrollState = scrollState,
            homeViewModel,
            tabSelectedCallBack = {
                homeViewModel.selectedTabIndex(it)
                splashViewModel.updateTheme(it)
                onBack.invoke(it)
            }, tabSelectedState = tabSelectedState
        )
    }
}

