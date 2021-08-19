package com.example.myfirstcomposeapp.project.fragment

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lib_common.utils.pxToDp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.canvas_ui.BoxBorderClipShape
import com.example.myfirstcomposeapp.canvas_ui.BoxClipShapes
import com.example.myfirstcomposeapp.utils.getBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

@Composable
fun OneFragment(modifier: Modifier?) {
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
            .height(250.dp)
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
            Log.e("canvase", "OneFragment:${size.height} " )
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
            path.moveTo(scrrenOffset.value.x, scrrenOffset.value.y)
            //随便绘制了哥区域。当然了为了好看曲线可以更美。
            if (xbLength > 0) {
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
                    scrrenOffset.value.x - 100, scrrenOffset.value.y - 100, 50f + xbLength,
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

@Composable
fun OneFragment1(modifier: Modifier?) {
    //设置滑动
      val scrollLazyState = rememberLazyListState()
//    var currentNumber = remember { mutableStateOf(0f) }
//    var offset = remember { mutableStateOf(Offset.Zero) }
//    val imageBitmap = getBitmap(resource = R.drawable.dmax)
//    Box {
//        androidx.compose.foundation.Canvas(
//            modifier = Modifier
//                .width(600.pxToDp())
//                .height(600.pxToDp())
//                .background(Color.Black)
//                .pointerInput(Unit) {
//                    detectHorizontalDragGestures(
//                        onDragStart = {
//
//                        },
//                        onHorizontalDrag = { change, dragAmount ->
//                            Log.e(
//                                "onHorizontalDrag",
//                                "OneFragment: dragAmount=$dragAmount;change=$change"
//                            )
//                            currentNumber.value = dragAmount
//                        },
//                        onDragCancel = {
//
//                        },
//                        onDragEnd = {
//
//                        }
//                    )
//                }
//                .draggable(
//                    orientation = Orientation.Horizontal,
//                    state = rememberDraggableState { delta ->
//                        if (delta > 0) {
//                            offset.value++
//                            if (currentNumber.value > 5) {
//                                currentNumber.value = 1
//                            }
//                            if (offset.value > 10) {
//                                currentNumber.value++
//                                offset.value = 0
//                            }
//
//                        }
//                        if (delta < 0) {
//                            offset.value++
//                            if (currentNumber.value < 0) {
//                                currentNumber.value = 5
//                            }
//                            if (offset.value > 10) {
//                                currentNumber.value--
//                                offset.value = 0
//                            }
//                        }
//                        Log.e("滑动水平距离delta=", delta.toString())
//                    }
//                )
//                .scrollable(
//                    orientation = Orientation.Horizontal,
//                    reverseDirection=true,
//                    state = rememberScrollableState { delta ->
//                        if (delta > 0) {
//                            offset.value++
//                            if (currenNumber.value > 5) {
//                                currenNumber.value = 1
//                            }
//                            if (offset.value > 10) {
//                                currenNumber.value++
//                                offset.value = 0
//                            }
//
//                        }
//                        if (delta < 0) {
//                            offset.value++
//                            if (currenNumber.value < 0) {
//                                currenNumber.value = 5
//                            }
//                            if (offset.value > 10) {
//                                currenNumber.value--
//                                offset.value = 0
//                            }
//                        }
//                        Log.e("滑动水平距离delta=", delta.toString())
//                        delta
//                    }
//                ),
//        ) {
//            drawIntoCanvas {
//                val mMatrix = android.graphics.Matrix()
//                mMatrix.run {
//                    reset()
//                    val slidingDistance = -(600 * (5.0 - (currentNumber.value - 1.0))).toFloat()
//                    this.postTranslate(slidingDistance, 0f)
//                    it.nativeCanvas.drawBitmap(imageBitmap.asAndroidBitmap(), this, null)
//                }
//            }
//        }
//    }
    LazyColumn(state = scrollLazyState) {
        //遍历循环内部Item部件
        items(20) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                StudyLayoutViews()

            }
        }
    }
}

@Composable
fun StudyLayoutViews() {
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(R.drawable.hean_lhc)
    val delectedIcon: ImageBitmap = ImageBitmap.imageResource(R.drawable.delected_icon)
    Box(
        modifier = Modifier
            .clip(BoxBorderClipShape)
            .background(Color(0XFF0DBEBF))
            .clickable(onClick = {})
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
                .clip(BoxClipShapes)
                .background(Color(206, 236, 250))
                .border(width = 1.dp, color = Color(0XFF0DBEBF), shape = BoxBorderClipShape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(3.dp)
            ) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "w",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(55.dp)
                        .width(55.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(3.dp)
                        .clip(
                            CircleShape
                        )
                        .shadow(elevation = 150.dp, clip = true)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    Text(
                        "Container",
                        fontSize = 18.sp,
                        color = Color.Black,
                    )
                    Text(
                        "123万阅读量",
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 3.dp, end = 2.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        bitmap = delectedIcon,
                        contentDescription = "w",
                        modifier = Modifier
                            .height(16.dp)
                            .shadow(elevation = 150.dp, clip = true)

                    )
                }
            }
        }
    }

}