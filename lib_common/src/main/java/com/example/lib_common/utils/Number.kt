package com.example.lib_common.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T : Number> Number.getSum(a: T, b: T): Float {
    return a.toFloat() + b.toFloat()
}

fun String?.notNull(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else {
        this
    }
}

fun String.splitEndContent(): String {
    return if (length <= 4) {
        this
    } else {
        substring(0,2)
    }
}