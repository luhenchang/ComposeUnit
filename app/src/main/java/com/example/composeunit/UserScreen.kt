package com.example.composeunit

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun UserScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val titleBarSize = 45.dp
    val targetHeight = 100.dp
    val targetHeightPx = with(LocalDensity.current) { targetHeight.roundToPx().toFloat() }
    val targetPercent by remember { mutableStateOf(Animatable(1f)) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            var dyConsumed = 0f

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                Log.e("delta::",delta.toString())
                dyConsumed += delta
                //0表示滑动顶部
                dyConsumed = dyConsumed.coerceAtMost(0f)
                Log.e("dyConsumed::",dyConsumed.toString())
                val percent = dyConsumed / targetHeightPx
                Log.e("percent::",percent.toString())
                coroutineScope.launch {
                    targetPercent.animateTo(1 - abs(percent.coerceIn(-1f, 0f)))
                }
                if (percent > -1 && percent < 0) {
                    Log.e("delta end::",delta.toString())
                    return Offset(0f, delta)
                }
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(R.color.colorAccent))
                .fillMaxWidth()
                .height(titleBarSize + targetHeight * targetPercent.value)
        ) {
            IconButton(
                modifier = Modifier.height(titleBarSize),
                onClick = {
                    if (context is AppCompatActivity) {
                        context.onBackPressedDispatcher.onBackPressed()
                    }
                }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = colorResource(R.color.white)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.alertdlog),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(titleBarSize * targetPercent.value.coerceAtLeast(0.75f))
                    .align(Alignment.Center)
                    .clip(CircleShape),
            )
            Text(
                text = "null",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        y = 35.dp * targetPercent.value
                    ),
                fontSize = 16.sp,
                color = colorResource(R.color.colorPrimary),
            )
            Text(
                text = "积分:",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = 55.dp * targetPercent.value)
                    .graphicsLayer {
                        alpha = targetPercent.value
                    },
                fontSize = 12.sp,
                color = colorResource(R.color.colorPrimary),
            )
        }
        LazyColumn(
            modifier = Modifier
                .background(Color(238, 239, 247, 255))) {
            //遍历循环内部Item部件
            items(30) { index ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(80.dp)
                        .background(Color.Green)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = "不错index=$index")
                }
            }
        }
    }

}