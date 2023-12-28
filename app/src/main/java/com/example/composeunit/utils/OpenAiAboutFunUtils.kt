package com.example.composeunit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Created by wangfei44 on 2023/4/9.
 */


fun getDelayTime(size: Int): Long {
    return if (size < 20) {
        100
    } else if (size < 100) {
        50
    } else {
        30
    }
}

@Composable
fun submitColor(isFocused: Boolean, textValue: String, loading: Boolean = false) =
    if (isFocused && textValue.isNotEmpty() && !loading) Color.White
    else Color(
        141, 141, 159, 255
    )

@Composable
fun inputColor(isFocused: Boolean, textValue: String, loading: Boolean = false) =
    if (isFocused && textValue.isNotEmpty() && !loading) Color.White
    else Color(
        141, 141, 159, 255
    )