package com.example.composeunit.ui.compose.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.ui.compose.home.HomeViewModel
import com.example.composeunit.ui.compose.SplashViewModel
import com.example.composeunit.ui.compose.home.widget.CollapsableLayout
import com.example.composeunit.ui.navigation.ExitComposeScreen


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    splashViewModel: SplashViewModel,
    onBack: (index: Int) -> Unit
) {
    ExitComposeScreen()
    homeViewModel.getInformation(LocalContext.current)
    val tabSelectedState by homeViewModel.tabSelectedIndex.collectAsState()
    CollapsableLayout(
        homeViewModel = homeViewModel,
        tabSelectedState = tabSelectedState,
        splashViewModel = splashViewModel,
        onBack = onBack
    )
}









