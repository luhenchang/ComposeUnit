package com.example.composeunit.ui.compose.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeunit.R
import com.example.composeunit.utils.getBitmap

import android.graphics.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow


@Preview
@Composable
fun InkColorCanvas() {
    val imageBitmap = getBitmap(R.drawable.csmr)
    val imageBitmapDefault = getBitmap(R.drawable.hbmr)
    val screenOffset = remember { mutableStateOf(Offset(0f, 0f)) }
    val animalState = remember { mutableStateOf(false) }

    val animal: Float by animateFloatAsState(
        if (animalState.value) {
            1f
        } else {
            0f
        }, animationSpec = TweenSpec(durationMillis = 6000),
        label = ""
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
                            screenOffset.value = Offset(position.x, position.y)
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
            val blackColorBitmap = Bitmap.createScaledBitmap(
                imageBitmapDefault.asAndroidBitmap(),
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
            canva.nativeCanvas.drawBitmap(blackColorBitmap, 0f, 0f, paint)
            //PorterDuffXfermode 设置画笔的图形混合模式
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            val xbLength =
                kotlin.math.sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2))
                    .toFloat() * animal
            //画圆
//            canva.nativeCanvas.drawCircle(
//                scrrenOffset.value.x,
//                scrrenOffset.value.y,
//                xbLength,
//                paint
//            )
            val path = Path().asAndroidPath()
            path.moveTo(screenOffset.value.x, screenOffset.value.y)
            //随便绘制了哥区域。当然了为了好看曲线可以更美。
            if (xbLength > 0) {
                path.addOval(
                    RectF(
                        screenOffset.value.x - xbLength,
                        screenOffset.value.y - xbLength,
                        screenOffset.value.x + 100f + xbLength,
                        screenOffset.value.y + 130f + xbLength
                    ), android.graphics.Path.Direction.CCW
                )
                path.addCircle(
                    screenOffset.value.x, screenOffset.value.y, 100f + xbLength,
                    android.graphics.Path.Direction.CCW
                )
                path.addCircle(
                    screenOffset.value.x - 100, screenOffset.value.y - 100, 50f + xbLength,
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