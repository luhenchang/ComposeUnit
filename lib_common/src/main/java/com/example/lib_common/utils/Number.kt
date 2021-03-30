package com.example.lib_common.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T : Number> Number.getSum(a: T, b: T): Float {
    return a.toFloat()+b.toFloat()
}
