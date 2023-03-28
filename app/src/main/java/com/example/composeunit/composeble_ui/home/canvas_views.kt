package com.example.composeunit.composeble_ui.home

import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.composeunit.R
import com.example.composeunit.utils.getBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Stable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
/**
 * Created by wangfei44 on 2023/3/24.
 */
@Composable
fun DrawableCanvas() {
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
    Canvas(modifier = Modifier
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
        }) {
        drawIntoCanvas { canva ->
            Log.e("canvase", "OneFragment:${size.height} ")
            val multiColorBitmpa = Bitmap.createScaledBitmap(
                imageBitmap.asAndroidBitmap(), size.width.toInt(), size.height.toInt(), false
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
            val xbLength =
                sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2))
                    .toFloat() * animal
            //画圆
//            canva.nativeCanvas.drawCircle(
//                scrrenOffset.value.x,
//                scrrenOffset.value.y,
//                xbLength,
//                paint
//            )
            val path = androidx.compose.ui.graphics.Path().asAndroidPath()
            path.moveTo(scrrenOffset.value.x, scrrenOffset.value.y)
            //随便绘制了哥区域。当然了为了好看曲线可以更美。
            if (xbLength > 0) {
                path.addOval(
                    RectF(
                        scrrenOffset.value.x - xbLength,
                        scrrenOffset.value.y - xbLength,
                        scrrenOffset.value.x + 100f + xbLength,
                        scrrenOffset.value.y + 130f + xbLength
                    ), Path.Direction.CCW
                )
                path.addCircle(
                    scrrenOffset.value.x,
                    scrrenOffset.value.y,
                    100f + xbLength,
                    Path.Direction.CCW
                )
                path.addCircle(
                    scrrenOffset.value.x - 100,
                    scrrenOffset.value.y - 100,
                    50f + xbLength,
                    Path.Direction.CCW
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