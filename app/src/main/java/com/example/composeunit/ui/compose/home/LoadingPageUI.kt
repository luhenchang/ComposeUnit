package com.example.composeunit.ui.compose.home

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun LoadingPageUI(index: Int) {
    val waveWidth = 30f
    val waveHeight = 21f
    val animalValue by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(170.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            drawIntoCanvas {
                val canvas: Canvas = it.nativeCanvas
                canvas.scale(1f, -1f)

                val roundRect = Path()
                roundRect.addRoundRect(
                    waveWidth * 4,
                    waveWidth * 3 * (1 - animalValue),
                    waveWidth * 8,
                    -waveWidth * 3,
                    60f,
                    60f,
                    Path.Direction.CCW
                )
                canvas.clipPath(roundRect)

                canvas.translate(animalValue * (waveWidth * 4), 0f) //内层海浪
                val wavePath = Path()
                wavePath.moveTo(0f, -waveWidth * 6)
                wavePath.lineTo(0f, 0f)
                wavePath.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
                wavePath.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
                wavePath.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
                wavePath.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
                wavePath.lineTo(waveWidth * 8, -waveWidth * 6)
                canvas.drawPath(wavePath, getPaintBefore(Paint.Style.FILL, waveWidth, index))

                canvas.translate(animalValue * (waveWidth * 4), 0f) //内层海浪
                val wavePathOut = Path()
                wavePathOut.moveTo(-waveWidth * 7, -waveWidth * 6)
                wavePathOut.lineTo(-waveWidth * 7, 0f)
                wavePathOut.quadTo(-waveWidth * 7, waveHeight, -waveWidth * 6, 0f)
                wavePathOut.quadTo(-waveWidth * 5, -waveHeight, -waveWidth * 4, 0f)
                wavePathOut.quadTo(-waveWidth * 3, waveHeight, -waveWidth * 2, 0f)
                wavePathOut.quadTo(-waveWidth, -waveHeight, 0f, 0f)
                wavePathOut.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
                wavePathOut.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
                wavePathOut.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
                wavePathOut.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
                wavePathOut.lineTo(waveWidth * 8, -waveWidth * 6)
                canvas.drawPath(wavePathOut, getPaint(Paint.Style.FILL, waveWidth, index))
            }
        }
    }
}

fun getPaintBefore(style: Paint.Style, waveWidth: Float, index: Int): Paint {
    val gPaint = Paint()
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = getColorForIndex(index).toArgb()
    val linearGradient = LinearGradient(
        waveWidth * 4, -waveWidth * 8,
        waveWidth * 4,
        80f,
        getColorForIndex(index).toArgb(),
        getColorEndForIndex(index).toArgb(),
        Shader.TileMode.CLAMP
    )
    gPaint.shader = linearGradient
    return gPaint
}

private fun getPaint(style: Paint.Style, waveWidth: Float, index: Int): Paint {
    val gPaint = Paint()
    gPaint.color = android.graphics.Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = android.graphics.Color.argb(255, 75, 151, 79)
    val linearGradient = LinearGradient(
        waveWidth * 4, -waveWidth * 2,
        waveWidth * 4,
        80f,
        getColorForIndex(index).toArgb(),
        getColorEndForIndex(index).toArgb(),
        Shader.TileMode.CLAMP
    )
    gPaint.shader = linearGradient
    return gPaint
}