package com.example.composeunit.project.fragment

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.ComposeData
import com.example.composeunit.R
import com.example.composeunit.canvas_ui.BoxBorderClipShape
import com.example.composeunit.canvas_ui.BoxClipShapes
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.utils.getBitmap
import com.example.lib_common.utils.notNull
import com.example.lib_common.utils.splitEndContent
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
            Log.e("canvase", "OneFragment:${size.height} ")
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
fun OneFragment1(homeViewModel: HomeViewModel = viewModel()) {
    homeViewModel.getInformation(LocalContext.current)
    //设置滑动
    val scrollLazyState = rememberLazyListState()
    val state = homeViewModel.itemUIState.collectAsState().value
    if (state.isNullOrEmpty()) {
        Text(text = "数据加载失败")
    } else {
        LazyColumn(
            state = scrollLazyState
        ) {
            //遍历循环内部Item部件
            items(state.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    StudyLayoutViews(state[index], homeViewModel)
                }
            }
        }
    }

}

@Composable
fun StudyLayoutViews(composeData: ComposeData, viewModel: HomeViewModel) {
    val current = LocalContext.current
    Box(
        modifier = Modifier
            .clip(BoxBorderClipShape)
            .background(Color(0XFF0DBEBF))
            .shadow(elevation = 33.dp, spotColor = Color.Red, ambientColor = Color.Yellow)
            .clickable(onClick = {
                viewModel.insertComposeData(current)
            })
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
                modifier = Modifier.padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(55.dp)
                        .width(55.dp)
                        .align(Alignment.CenterVertically)
                        .border(2.dp, color = Color(238, 204, 203, 255), shape = CircleShape)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape
                        )
                        .background(
                            Color(13, 189, 190, 193), shape = CircleShape
                        )
                ) {
                    Text(
                        composeData.item_title.notNull().splitEndContent(),
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.Center),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            shadow = Shadow(
                                color = Color(32, 3, 37, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    Text(
                        composeData.item_title.notNull(),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 10.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            shadow = Shadow(
                                color = Color(42, 7, 48, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                    Text(
                        composeData.item_content.notNull(),
                        color = Color(42, 7, 48, 255),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier
                            .padding(bottom = 20.dp, end = 20.dp),
                        fontStyle = FontStyle.Italic,
                        style = TextStyle(
                            fontSize = 15.sp,
                            shadow = Shadow(
                                color = Color(42, 7, 48, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                }
            }
        }
    }

}

fun String?.removeNull(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else {
        this
    }
}