package com.example.composeunit.composeble_ui.home

import android.graphics.text.MeasuredText
import android.os.Build
import android.view.View
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.draggable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.drawText
import androidx.core.view.ViewCompat.setLayerType

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
    scaleH: Float
) {
    val tabHeight by animateDpAsState(
        targetValue = if (tabSelectedState.value == index) {
            120.dp
        } else {
            100.dp
        }, animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (tabSelectedState.value == index) tabHeight.value.dp * scaleH else 100.dp * scaleH)
                .clickable(onClick = {
                    tabSelectedState.value = index
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


@Composable
fun TabText(modifier: Modifier) {
    Text(
        "Te",
        fontSize = 14.sp,
        color = Color.White,
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold, fontSize = 15.sp, shadow = Shadow(
                color = Color(32, 3, 37, 100),
                offset = Offset(2f, 3f),
                blurRadius = 3f
            )
        )
    )
}

