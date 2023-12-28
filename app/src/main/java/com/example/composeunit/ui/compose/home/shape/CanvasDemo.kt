package com.example.composeunit.ui.compose.home.shape

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CanvasDemo() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clickable(enabled = false) {},
        onDraw = {
            drawIntoCanvas { it ->
                val height = size.height
                val width = size.width
                val canvas = it.nativeCanvas
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(0f, height)
                path.lineTo(width / 2f, height)

                //6.3f 5.56f 5.5f
                val scale = 0.5f
                val lengthControl = 0.3f
                val startHScale = 5.6f
                val control1HScale = 5.3f
                val control2HScale = 5.3f

                path.lineTo(width / 2f, height/10*startHScale)

                path.cubicTo(width/2f,height/10*control1HScale,width/2f+160f,height/10*control2HScale,width/2f+160f,height/10*5f)
                path.cubicTo(width/2f+160f,height-height/10*control2HScale,width/2f,height-height/10*control1HScale,width/2f,height-height/10*startHScale)

                path.lineTo(width / 2f, 0f)
                path.close()
                canvas.drawPath(path, Paint().apply {
                    color = android.graphics.Color.RED
                })
            }
        })
}