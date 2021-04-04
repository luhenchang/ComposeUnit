package com.example.lib_common.utils

/**
 * androidx.compose.ui.graphics.Color
 */
fun composeColor(alpha: Int, red: Int, green: Int, blue: Int): androidx.compose.ui.graphics.Color =
    androidx.compose.ui.graphics.Color(red, green, blue, alpha)
fun composeColorLong(int: Long):androidx.compose.ui.graphics.Color =
    androidx.compose.ui.graphics.Color(int)
/**
 * android.graphics.Color
 */
fun nativeColor(alpha: Int, red: Int, green: Int, blue: Int): Int =
    android.graphics.Color.argb(red, green, blue, alpha)