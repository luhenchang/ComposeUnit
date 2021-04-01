package com.example.lib_common.utils
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Number.pxToDp(): Dp {

    return (this.toFloat() / Density(LocalContext.current).density).dp
}
