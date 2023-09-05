package com.example.composeunit.project.fragment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.home.widget.CollapsableLayout


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    splashViewModel: SplashViewModel,
    onBack: (index: Int) -> Unit
) {
    homeViewModel.getInformation(LocalContext.current)
    val tabSelectedState by homeViewModel.tabSelectedIndex.collectAsState()
    CollapsableLayout(
        homeViewModel = homeViewModel,
        tabSelectedState = tabSelectedState,
        splashViewModel = splashViewModel,
        onBack = onBack
    )
}







