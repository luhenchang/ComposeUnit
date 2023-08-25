package com.example.composeunit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val openAiDark = Color(52, 54, 65, 255)
val openAiLight = Color(68, 70, 84, 255)
@Composable
fun PlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themType: ThemeType = ThemeType.GREEN_THEM,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        getThemeByThemType(themType)
    } else {
        getThemeByThemType(themType)
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

