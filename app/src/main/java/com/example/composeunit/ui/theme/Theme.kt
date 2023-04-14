package com.example.composeunit.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val openAiDark = Color(52, 54, 65, 255)
val openAiLight = Color(68, 70, 84, 255)

val blue =  Color(0xFF108888)
val blueDark = Color(0xFF0B182E)

val backgroundDark = Color.White
val Purple300 = Color(0xFFCD52FC)

@SuppressLint("ConflictingOnColor")
private val PlayThemeLight = lightColors(
    primary = blue,
    onPrimary = Color.White,
    primaryVariant = blue,
    secondary = blue
)

@SuppressLint("ConflictingOnColor")
private val PlayThemeDark = darkColors(
    primary = blueDark,
    onPrimary = Color.White,
    secondary = blueDark,
    surface = blueDark,
    background = backgroundDark
)
@Composable
fun PlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        PlayThemeDark
    } else {
        PlayThemeLight
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}
private val DarkColorPalette = darkColors(
        primary = Purple200,
        primaryVariant = Purple700,
        secondary = Teal200
)

private val LightColorPalette = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xFF0DBEBF)
// ...

val DarkColors = darkColors(
    primary = Yellow200,
    secondary = Blue200,
    // ...
)

@Composable
fun MyFirstComposeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = Shapes,
            content = content
    )
}