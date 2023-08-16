package com.example.composeunit.composeble_ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.lib_common.utils.pxToDp

/**
 * Created by wangfei44 on 2023/3/24.
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ComposeTabView(
    tabTitle: String = "TV",
    modifier: Modifier,
    index: Int,
    tabSelectedState: MutableState<Int>,
    heightValue: Float,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val headerHeightPx = with(LocalDensity.current) {
        120.dp.roundToPx().toFloat()
    }
    val headerOtherHeightPx = with(LocalDensity.current) {
        100.dp.roundToPx().toFloat()
    }
    val endHeight =headerHeightPx - heightValue
    val endOtherHeight =headerOtherHeightPx - heightValue

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (tabSelectedState.value == index) endHeight.pxToDp() else endOtherHeight.pxToDp())
                .clickable(onClick = {
                    if (tabSelectedState.value != index) {
                        tabSelectedState.value = index
                        homeViewModel.getInformation(context)
                    }
                }, indication = null, interactionSource = remember {
                    MutableInteractionSource()
                }),
            onDraw = {
                drawIntoCanvas { canvas ->
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, size.height - 45)
                        quadraticBezierTo(
                            5f,
                            size.height - 5,
                            45f, size.height
                        )
                        lineTo(size.width - 45, size.height)
                        quadraticBezierTo(
                            size.width - 5,
                            size.height - 5,
                            size.width,
                            size.height - 45
                        )
                        lineTo(size.width, 0f)
                        close()
                    }
                    val paint = Paint()
                    paint.color = getColorForIndex(index)
                    paint.style = PaintingStyle.Fill
                    paint.isAntiAlias = true
                    canvas.drawPath(path, paint)
                    /**调用底层fragment Paint绘制*/
                    val pathShadow = Path().apply {
                        moveTo(0f, size.height - 44)
                        quadraticBezierTo(
                            5f,
                            size.height - 5,
                            44f, size.height
                        )
                        lineTo(size.width - 44, size.height)
                        quadraticBezierTo(
                            size.width - 5,
                            size.height - 5,
                            size.width,
                            size.height - 44
                        )
                        moveTo(size.width, size.height - 44)
                    }
                    val frameworkPaint = Paint().asFrameworkPaint()
                    frameworkPaint.style = android.graphics.Paint.Style.STROKE
                    frameworkPaint.color = getColorForIndex(tabSelectedState.value).toArgb()
                    frameworkPaint.isAntiAlias = true
                    frameworkPaint.textSize = 36f
                    /**绘制阴影*/
                    frameworkPaint.setShadowLayer(
                        22f,
                        0f,
                        20f,
                        Color(0.0f, 0.588f, 0.533f, 0.5f).toArgb()
                    )
                    canvas.drawPath(pathShadow, frameworkPaint.toComposePaint())
                    frameworkPaint.textSize = 36f
                    frameworkPaint.color = Color.White.toArgb()
                    frameworkPaint.setShadowLayer(
                        6f,
                        0f,
                        3f,
                        getColorForIndex(tabSelectedState.value).toArgb()
                    )
                    val rect = android.graphics.Rect()
                    frameworkPaint.getTextBounds(tabTitle, 0, 2, rect)
                    canvas.nativeCanvas.drawText(
                        tabTitle,
                        size.width / 2f - rect.width() / 2,
                        size.height * 2 / 3f,
                        frameworkPaint.apply {
                            style = android.graphics.Paint.Style.STROKE
                        }
                    )
                }
            }
        )
        Box(Modifier.height(4.dp))
        AnimatedVisibility(
            visible = tabSelectedState.value != index,
            enter = fadeIn() + scaleIn(animationSpec = tween(300)),
            exit = fadeOut() + scaleOut(animationSpec = tween(300))
        ) {
            Box(
                Modifier
                    .size(13.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = getColorForIndex(tabSelectedState.value),
                        spotColor = getColorForIndex(tabSelectedState.value),
                        clip = false
                    )
                    .background(getColorForIndex(index), shape = CircleShape)
            )
        }
    }
}

fun getColorForIndex(index: Int): Color {
    return when (index) {
        0 -> Color(0xFF108888)
        1 -> Color(0xFF9C27B0)
        2 -> Color(0xFFFFC107)
        3 -> Color(0xFF4CAF50)
        4 -> Color(0xFFF44336)
        5 -> Color(0xFF673AB7)
        else -> Color(0xFFCDDC39)
    }
}

fun getColorEndForIndex(index: Int): Color {
    return when (index) {
        0 -> Color(0xFF0CF3F3)
        1 -> Color(0xFF3A0144)
        2 -> Color(0xFFF5734C)
        3 -> Color(0xFF31E739)
        4 -> Color(0xFF740A03)
        5 -> Color(0xFFC9B4EE)
        else -> Color(0xFFEEB303)
    }
}

