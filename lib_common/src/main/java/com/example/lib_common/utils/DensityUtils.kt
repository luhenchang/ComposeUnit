package com.example.lib_common.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Number.pxToDp(): Dp {
    return (this.toFloat() / Density(LocalContext.current).density).dp
}
