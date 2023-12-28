package com.example.composeunit.ui.compose.other.canvas

import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun NodeCanvas() {
    var tapValue by remember { mutableStateOf(Offset(0f, 0f)) }
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    tapValue = it
                }, onTap = {

                })
            }) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas {
                val path = Path()
                path.addCircle(
                    size.width / 2f,
                    size.height / 2f,
                    40f, Path.Direction.CCW
                )
                if (pointInPath(path, tapValue)) {
                    it.nativeCanvas.drawCircle(size.width / 2f - 100f,
                        size.height / 2f + 200f,
                        40f,
                        Paint().asFrameworkPaint().apply {
                            color = Color.RED
                            style = android.graphics.Paint.Style.FILL
                        })
                    it.nativeCanvas.drawLine(size.width / 2f,
                        size.height / 2f,
                        size.width / 2f - 100f,
                        size.height / 2f + 200f,
                        Paint().asFrameworkPaint().apply {
                            color = Color.RED
                            style = android.graphics.Paint.Style.STROKE
                        })
                }
                it.nativeCanvas.drawCircle(size.width / 2f,
                    size.height / 2f,
                    40f,
                    Paint().asFrameworkPaint().apply {
                        color = Color.RED
                        style = android.graphics.Paint.Style.FILL
                    })
//
//                it.nativeCanvas.drawCircle(size.width / 2f - 100f,
//                    size.height / 2f + 200f,
//                    40f,
//                    Paint().asFrameworkPaint().apply {
//                        color = Color.RED
//                        style = android.graphics.Paint.Style.FILL
//                    })
//                it.nativeCanvas.drawLine(size.width / 2f,
//                    size.height / 2f,
//                    size.width / 2f - 100f,
//                    size.height / 2f + 200f,
//                    Paint().asFrameworkPaint().apply {
//                        color = Color.RED
//                        style = android.graphics.Paint.Style.STROKE
//                    })
//                it.nativeCanvas.drawCircle(size.width / 2f + 100f,
//                    size.height / 2f + 200f,
//                    40f,
//                    Paint().asFrameworkPaint().apply {
//                        color = Color.RED
//                        style = android.graphics.Paint.Style.FILL
//                    })
//
//                it.nativeCanvas.drawCircle(size.width / 2f + 100f,
//                    size.height / 2f + 200f,
//                    40f,
//                    Paint().asFrameworkPaint().apply {
//                        color = Color.RED
//                        style = android.graphics.Paint.Style.FILL
//                    })
//
//                it.nativeCanvas.drawLine(size.width / 2f,
//                    size.height / 2f,
//                    size.width / 2f + 100f,
//                    size.height / 2f + 200f,
//                    Paint().asFrameworkPaint().apply {
//                        color = Color.RED
//                        style = android.graphics.Paint.Style.STROKE
//                    })


            }
        }
    }
}

private fun pointInPath(path: Path, point: Offset): Boolean {
    val bounds = RectF()
    path.computeBounds(bounds, true)
    val region = Region()
    region.setPath(
        path,
        Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt(), bounds.bottom.toInt())
    )
    return region.contains(point.x.toInt(), point.y.toInt())
}