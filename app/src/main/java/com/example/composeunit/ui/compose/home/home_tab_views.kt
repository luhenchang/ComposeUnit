package com.example.composeunit.ui.compose.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.home.widget.HomeScrollState
import com.example.composeunit.ui.theme.getPrimaryColorForIndex
import com.example.lib_common.utils.pxToDp

/**
 * Created by wangfei44 on 2023/3/24.
 */

@Composable
fun ComposeTabView(
    tabTitle: String = "",
    modifier: Modifier,
    index: Int,
    tabSelectedCallBack: (Int) -> Unit,
    scrollState: HomeScrollState,
    tabSelectedState: Int,
    homeViewModel: HomeViewModel = viewModel(),
    selectedHeaderHeight: Dp = 120.dp,
    defaultHeaderHeight: Dp = 100.dp,
) {
    val context = LocalContext.current
    val headerHeightPx = with(LocalDensity.current) {
        selectedHeaderHeight.roundToPx().toFloat()
    }
    val headerOtherHeightPx = with(LocalDensity.current) {
        defaultHeaderHeight.roundToPx().toFloat()
    }
    val selectedHeaderHeightPx = headerHeightPx - scrollState.getScrollYOffset()
    val unSelectedHeaderHeightPx = headerOtherHeightPx - scrollState.getScrollYOffset()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (tabSelectedState == index) selectedHeaderHeightPx.pxToDp() else unSelectedHeaderHeightPx.pxToDp())
                .clickable(onClick = {
                    if (tabSelectedState != index) {
                        tabSelectedCallBack.invoke(index)
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
                    paint.color = getPrimaryColorForIndex(index)
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
                    frameworkPaint.color = getPrimaryColorForIndex(tabSelectedState).toArgb()
                    frameworkPaint.isAntiAlias = true
                    frameworkPaint.textSize = 36f
                    /**绘制阴影*/
                    frameworkPaint.setShadowLayer(
                        22f,
                        0f,
                        20f,
                        Color(0.0f, 0.588f, 0.533f, 0.5f).toArgb()
                    )
                    canvas.drawPath(pathShadow, frameworkPaint.asComposePaint())
                    frameworkPaint.textSize = 36f
                    frameworkPaint.color = Color.White.toArgb()
                    frameworkPaint.setShadowLayer(
                        6f,
                        0f,
                        3f,
                        getPrimaryColorForIndex(tabSelectedState).toArgb()
                    )
                    val rect = android.graphics.Rect()
                    frameworkPaint.getTextBounds(tabTitle, 0, tabTitle.length, rect)
                    canvas.nativeCanvas.drawText(
                        tabTitle,
                        (size.width - rect.width()) / 2,
                        size.height * 2 / 3f,
                        frameworkPaint.apply {
                            style = android.graphics.Paint.Style.FILL
                        }
                    )
                }
            }
        )
        Box(Modifier.height(4.dp))
        AnimatedVisibility(
            visible = tabSelectedState != index,
            enter = fadeIn() + scaleIn(animationSpec = tween(300)),
            exit = fadeOut() + scaleOut(animationSpec = tween(300))
        ) {
            Box(
                Modifier
                    .size(13.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = getPrimaryColorForIndex(tabSelectedState),
                        spotColor = getPrimaryColorForIndex(tabSelectedState),
                        clip = false
                    )
                    .background(getPrimaryColorForIndex(index), shape = CircleShape)
            )
        }
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

