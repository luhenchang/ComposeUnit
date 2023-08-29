package com.example.composeunit.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
//     primary: Color, // 主颜色，屏幕和元素都用这个颜色
//     primaryVariant: Color, // 用于区分主颜色，比如app bar和system bar
//     secondary: Color, // 强调色，悬浮按钮，单选/复选按钮，高亮选中的文本，链接和标题
//     secondaryVariant: Color, // 用于区分强调色
//     background: Color, // 背景色，在可滚动项下面展示
//     surface: Color, // 表层色，展示在组件表层，比如卡片，清单和菜单(CardView,SheetLayout,Menu)等
//     error: Color, // 错误色，展示错误信息，比如TextField的提示信息
//     onPrimary: Color, // 在主颜色primary之上的文本和图标的颜色
//     onSecondary: Color, // 在强调色secondary之上的文本和图标的颜色
//     onBackground: Color, // 在背景色background之上的文本和图标的颜色
//     onSurface: Color, // 在表层色surface之上的文本和图标的颜色
//     onError: Color, // 在错误色error之上的文本和图标的颜色
//     isLight: Boolean // 是否是浅色模式
//
val StatusBarColor = Color(0x0)
fun getThemeByThemType(themType: ThemeType): Colors {
    return when (themType) {
        ThemeType.GREEN_THEM -> {
            GreenThemeLight
        }
        ThemeType.RED_THEM -> {
            REDThemeLight
        }
        ThemeType.PURPLE_THEM -> {
            PURPLEThemeLight
        }
        ThemeType.BLUE_THEM -> {
            BLUEThemeLight
        }
        ThemeType.ORANGE_THEM -> {
            ORANGEThemeLight
        }
        ThemeType.LIGHT_BLUE_THEM -> {
            LightBlueThemeLight
        }
        ThemeType.YELLOW_THEM -> {
            YELLOWThemeLight
        }
    }
}
val primary_green = Color(0xFF40C756)
val primary_background = Color(0xFFCEEDD2)
private val GreenThemeLight: Colors = Colors(
    primary = primary_green,
    primaryVariant = primary_green,
    secondary = primary_green,
    secondaryVariant = primary_green,
    background = primary_background,
    surface = primary_green,
    error = primary_green,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)

val red_primary = Color(0xFFFF2E4B)
val red_background= Color(0xFFFCCBD0)

private val REDThemeLight : Colors = Colors(
    primary = red_primary,
    primaryVariant = red_primary,
    secondary = red_primary,
    secondaryVariant = red_primary,
    background = red_background,
    surface = red_primary,
    error = red_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)
val purple_primary = Color(0xFFB676FD)
val purple_background = Color(0xFFE9D3F5)

private val PURPLEThemeLight : Colors = Colors(
    primary = purple_primary,
    primaryVariant = purple_primary,
    secondary = purple_primary,
    secondaryVariant = purple_primary,
    background = purple_background,
    surface = purple_primary,
    error = purple_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)
val blue_primary = Color(0xFF4DD0E1)
val blue_background = Color(0xFFCBF5F9)

private val BLUEThemeLight : Colors = Colors(
    primary = blue_primary,
    primaryVariant = blue_primary,
    secondary = blue_primary,
    secondaryVariant = blue_primary,
    background = blue_background,
    surface = blue_primary,
    error = blue_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)

val orange_primary = Color(0xFFFF9900)
val orange_background = Color(0xFFFFDAAB)

private val ORANGEThemeLight : Colors = Colors(
    primary = orange_primary,
    primaryVariant = orange_primary,
    secondary = orange_primary,
    secondaryVariant = orange_primary,
    background = orange_background,
    surface = orange_primary,
    error = orange_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)
val light_blue_primary = Color(0xFF00FFF0)
val light_blue_background = Color(0xFFB5FFF6)

private val LightBlueThemeLight : Colors = Colors(
    primary = light_blue_primary,
    primaryVariant = light_blue_primary,
    secondary = light_blue_primary,
    secondaryVariant = light_blue_primary,
    background = light_blue_background,
    surface = light_blue_primary,
    error = light_blue_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)

val yellow_primary = Color(0xFFE3E23D)
val yellow_purple_background = Color(0xFFF4EECA)

private val YELLOWThemeLight : Colors = Colors(
    primary = yellow_primary,
    primaryVariant = yellow_primary,
    secondary = yellow_primary,
    secondaryVariant = yellow_primary,
    background = yellow_purple_background,
    surface = yellow_primary,
    error = yellow_primary,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)

fun getPrimaryColorForIndex(index: Int): Color {
    return when (index) {
        0 -> blue_primary
        1 -> red_primary
        2 -> purple_primary
        3 -> primary_green
        4 -> orange_primary
        5 -> light_blue_primary
        6 -> yellow_primary
        else -> primary_green
    }
}