package com.example.composeunit.ui.compose.canvas_ui

import android.R.attr
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeunit.R
import com.example.composeunit.utils.getBitmap
import android.R.attr.radius

import android.R.attr.centerY

import android.R.attr.centerX

import android.opengl.ETC1.getHeight

import android.opengl.ETC1.getWidth

import android.R.attr.bitmap
import android.graphics.*
import android.graphics.Color
import android.opengl.ETC1
import android.view.View
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat

import androidx.core.view.ViewCompat.setLayerType
import com.example.lib_common.utils.pxToDp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Math.pow
import java.lang.Math.sqrt
import kotlin.math.pow


@Preview
@Composable
fun InkColorCanvas() {
    val imageBitmap = getBitmap(R.drawable.csmr)
    val imageBitmap_default = getBitmap(R.drawable.hbmr)
    val scrrenOffset = remember { mutableStateOf(Offset(0f, 0f)) }
    val animalState = remember { mutableStateOf(false) }

    val animal: Float by animateFloatAsState(
        if (animalState.value) {
            1f
        } else {
            0f
        }, animationSpec = TweenSpec(durationMillis = 6000)
    )
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val position = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        launch {
                            scrrenOffset.value = Offset(position.x, position.y)
                            animalState.value = !animalState.value
                        }

                    }
                }
            }
    ) {
        drawIntoCanvas { canva ->
            val multiColorBitmpa = Bitmap.createScaledBitmap(
                imageBitmap.asAndroidBitmap(),
                size.width.toInt(),
                size.height.toInt(), false
            )
            val blackColorBitmpa = Bitmap.createScaledBitmap(
                imageBitmap_default.asAndroidBitmap(),
                size.width.toInt(),
                size.height.toInt(),
                false
            )
            val paint = Paint().asFrameworkPaint()
            canva.nativeCanvas.drawBitmap(multiColorBitmpa, 0f, 0f, paint) //绘制图片
            //保存图层
            val layerId: Int = canva.nativeCanvas.saveLayer(
                0f,
                0f,
                size.width,
                size.height,
                paint,
            )
            canva.nativeCanvas.drawBitmap(blackColorBitmpa, 0f, 0f, paint)
            //PorterDuffXfermode 设置画笔的图形混合模式
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            val xbLength = kotlin.math.sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2)).toFloat() * animal
            //画圆
//            canva.nativeCanvas.drawCircle(
//                scrrenOffset.value.x,
//                scrrenOffset.value.y,
//                xbLength,
//                paint
//            )
            val path = Path().asAndroidPath()
            path.moveTo(scrrenOffset.value.x, scrrenOffset.value.y)
            //随便绘制了哥区域。当然了为了好看曲线可以更美。
            if (xbLength>0) {
                path.addOval(
                    RectF(
                        scrrenOffset.value.x - xbLength,
                        scrrenOffset.value.y - xbLength,
                        scrrenOffset.value.x + 100f + xbLength,
                        scrrenOffset.value.y + 130f + xbLength
                    ), android.graphics.Path.Direction.CCW
                )
                path.addCircle(
                    scrrenOffset.value.x, scrrenOffset.value.y, 100f + xbLength,
                    android.graphics.Path.Direction.CCW
                )
                path.addCircle(
                    scrrenOffset.value.x-100, scrrenOffset.value.y-100, 50f + xbLength,
                    android.graphics.Path.Direction.CCW
                )
            }
            path.close()
            canva.nativeCanvas.drawPath(path, paint)
            //画布斜边
            paint.xfermode = null
            canva.nativeCanvas.restoreToCount(layerId)
        }
    }
}