package com.example.composeunit.ui.compose

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeunit.R
import com.example.composeunit.ui.compose.other.confing.MainActions

//启动页面 StartPageScreen
@Preview
@Composable
fun StartPageScreen(actions: MainActions? = null) {
    val rotationState = remember { mutableFloatStateOf(0f) }
    val animalValue by animateFloatAsState(
        targetValue = rotationState.floatValue,
        label = "",
        animationSpec = TweenSpec(durationMillis = 2000),
        finishedListener = {
            actions?.loginPage?.invoke()
        }
    )
    LaunchedEffect(Unit) {
        rotationState.floatValue = 1f
    }
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.splash_icon))
                    .offset(y = (-animalValue * 100).dp)
                    .rotate(360 * animalValue),
                painter = painterResource(R.mipmap.jetpack_icon),
                contentDescription = ""
            )

            Image(
                painter = painterResource(R.drawable.head_god),
                contentDescription = "w",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.splash_head_icon))
                    .offset(y = (-(1 - animalValue) * 100).dp)
                    .background(color = Color(0XFF0DBEBF), shape = CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .shadow(elevation = 150.dp, clip = true)
            )
        }
    }

}