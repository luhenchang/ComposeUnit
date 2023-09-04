package com.example.composeunit.project

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.example.base.ui.PermissionActivity
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.compose.confing.NavGraph
import com.example.composeunit.ui.theme.ComposeUnitTheme

class MainActivity : PermissionActivity() {
    private val viewModel: SplashViewModel by viewModels()
    override fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val themeType = viewModel.themTypeState.collectAsState().value
            ComposeUnitTheme(themType = themeType) {
                NavGraph(viewModel = viewModel)
            }
        }
    }
}
