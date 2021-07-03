package com.example.myfirstcomposeapp.project.fragment

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lib_common.utils.pxToDp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.canvas_ui.BoxBorderClipShape
import com.example.myfirstcomposeapp.canvas_ui.BoxClipShapes
import com.example.myfirstcomposeapp.utils.getBitmap

@Composable
fun OneFragment(modifier: Modifier) {
    //设置滑动
    val scrollLazyState = rememberLazyListState()
    var currenNumber = remember { mutableStateOf(1) }
    var offset = remember { mutableStateOf(0) }
    val imageBitmap = getBitmap(resource = R.drawable.dmax)
    Box {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .width(600.pxToDp())
                .height(600.pxToDp())
                .background(Color.Black)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (delta > 0) {
                            offset.value++
                            if (currenNumber.value > 5) {
                                currenNumber.value = 1
                            }
                            if (offset.value > 10) {
                                currenNumber.value++
                                offset.value = 0
                            }

                        }
                        if (delta < 0) {
                            offset.value++
                            if (currenNumber.value < 0) {
                                currenNumber.value = 5
                            }
                            if (offset.value > 10) {
                                currenNumber.value--
                                offset.value = 0
                            }
                        }
                        Log.e("滑动水平距离delta=", delta.toString())
                    }
                )
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
        ) {
            drawIntoCanvas {
                val mMatrix = android.graphics.Matrix()
                mMatrix.run {
                    reset()
                    val slidingDistance = -(600 * (5 - (currenNumber.value - 1))).toFloat()
                    this.postTranslate(slidingDistance, 0f)
                    it.nativeCanvas.drawBitmap(imageBitmap.asAndroidBitmap(), this, null)
                }
            }
        }
    }
//    LazyColumn(state = scrollLazyState) {
//        //遍历循环内部Item部件
//        items(20) {
//            Box(
//                modifier = Modifier
//                    .padding(10.dp)
//                    .fillMaxWidth(),
//            ) {
//                StudyLayoutViews()
//
//            }
//        }
//    }
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
                .border(width = 1.dp, color = Color(0XFF0DBEBF), shape = BoxClipShapes)
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