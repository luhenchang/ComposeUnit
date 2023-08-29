package com.example.composeunit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val openAiDark = Color(52, 54, 65, 255)
val openAiLight = Color(68, 70, 84, 255)
@Composable
fun ComposeUnitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themType: ThemeType = ThemeType.GREEN_THEM,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        getThemeByThemType(themType)
    } else {
        getThemeByThemType(themType)
    }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(StatusBarColor,true)
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

