package com.example.myfirstcomposeapp.canvas_ui

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R




@Composable
fun MyLineView(bitmap: ImageBitmap) {
    Canvas(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp)
            .height(100.dp),
    ) {
        drawIntoCanvas { canvas ->
            val paint = androidx.compose.ui.graphics.Paint()
            paint.color = Color(208, 208, 208)
            paint.style = PaintingStyle.Stroke
            paint.strokeWidth = 3f


            val paintFill = androidx.compose.ui.graphics.Paint()
            paintFill.color = Color.Gray
            paintFill.style = PaintingStyle.Stroke
            paintFill.strokeWidth = 3f

            //1.绘制坐标轴
            canvas.translate(0f, size.height)
            canvas.scale(1f, -1f)
            //2.绘制x轴
            val pathY = Path()
            pathY.moveTo(0f, 0f)
            pathY.relativeLineTo(0f, size.height)
            canvas.drawPath(pathY, paint)


            //2.绘制y轴
            val pathX = Path()
            pathX.moveTo(0f, 0f)
            pathX.relativeLineTo(size.width, 0f)
            canvas.drawPath(pathX, paint)


            val dataList: MutableList<Offset> = mutableListOf(
                Offset(20f, 60f),
                Offset(40f, 30f),
                Offset(50f, 34f),
                Offset(80f, 54f),
                Offset(100f, 34f),
                Offset(200f, 134f),
                Offset(400f, 154f),
                Offset(480f, 134f)
            )
            val linePath = Path()
            paint.color = Color.Blue
            val colors: MutableList<Color> = mutableListOf(Color.Red, Color.Blue, Color.Green)
            paint.shader = LinearGradientShader(
                Offset(0f, 0f),
                Offset(size.width, 0f),
                colors,
                null,
                TileMode.Clamp
            )
            paint.isAntiAlias = true

            //3.绘制折线填充
            for (index in 0 until dataList.size) {
                linePath.lineTo(dataList[index].x, dataList[index].y)
            }
            linePath.lineTo(dataList[dataList.size - 1].x, 0f)
            linePath.close()
            //绘制填充
            paintFill.style = PaintingStyle.Fill
            paintFill.shader = LinearGradientShader(
                Offset(0f, size.height),
                Offset(0f, 0f),
                arrayListOf(Color(59, 157, 254, 161), Color(59, 157, 254, 21)),
                null,
                TileMode.Clamp
            )
            canvas.drawPath(linePath, paintFill)

            //2绘制折线2
            val line = Path()
            for (index in 0 until dataList.size) {
                line.lineTo(dataList[index].x, dataList[index].y)
            }
            paint.style = PaintingStyle.Stroke
            canvas.drawPath(line, paint)


            //3绘制圆圈
            paint.style = PaintingStyle.Fill
            for (index in 0 until dataList.size) {
                canvas.drawCircle(Offset(dataList[index].x, dataList[index].y), 6f, paint)
            }
            canvas.drawImage(image = bitmap, Offset(100f, 100f), paint)

            //我醉了呀大哥
            val paints = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
            canvas.nativeCanvas.drawText("Hello", center.x, center.y, paints)


        }
    }

}
@Composable
fun TextDemo() {
    Canvas(modifier = Modifier.fillMaxWidth(),
        onDraw = {
            val paint = Paint()
            paint.textSize = 64f
            paint.color = 0xffff0000.toInt()
            drawIntoCanvas {
            }
        })
}






@Preview(name = "studyImage")
@Composable
fun StudyImageView() {
    val draggerOffset: MutableState<Float> = remember { mutableStateOf(0f) }
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(R.drawable.meinv)
    Image(
        bitmap = imageBitmap,
        contentScale = ContentScale.FillBounds,
        contentDescription ="",
        modifier = Modifier
            .height(260.dp)
            .width(200.dp)
            .padding(horizontal = 30.dp, vertical = 30.dp)
            .clip(
                RectangleImageShapes
            )
            .rotate(10f)
            .draggable(state = DraggableState(onDelta = {
                draggerOffset.value = +it
                Log.e("ondelta", "StudyImageView: " + draggerOffset.value)
            }), orientation = Orientation.Horizontal)
            .offset(x = draggerOffset.value.dp)
    )
}



@Stable
val RectangleImageShapes: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.relativeLineTo(20f, 20f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.relativeCubicTo(40f, 40f, 60f, 60f, -20f, 130f)
        path.lineTo(size.width, size.height)
        path.lineTo(size.width, 0f)
        path.close()
        return Outline.Generic(path)
    }
}

@Stable
class QureytoImageShapes(var hudu: Float = 100f, var controller:Float=0f) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, size.height - hudu)
        if(controller==0f){
            controller =size.width / 2f
        }
        path.quadraticBezierTo(controller, size.height, size.width, size.height - hudu)
        path.lineTo(size.width, 0f)
        path.close()
        return Outline.Generic(path)
    }
}


@Stable
val BoxClipShapes: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(20f, 0f)
        path.relativeLineTo(-20f, 20f)
        path.relativeLineTo(0f, size.height - 40f)
        path.relativeLineTo(20f, 20f)
        path.relativeLineTo(size.width / 3f - 20, 0f)
        path.relativeLineTo(15f, -20f)
        path.relativeLineTo(size.width / 3f - 30, 0f)
        path.relativeLineTo(15f, 20f)
        path.relativeLineTo(size.width / 3f - 20, 0f)
        path.relativeLineTo(20f, -20f)
        path.relativeLineTo(0f, -(size.height - 40f))
        path.relativeLineTo(-20f, -20f)
        path.relativeLineTo(-(size.width / 3f - 20), 0f)
        path.relativeLineTo(-15f, 10f)
        path.relativeLineTo(-(size.width / 3f - 30), 0f)
        path.relativeLineTo(-15f, -10f)
        path.close()

        return Outline.Generic(path)
    }
}

@Stable
val BoxBorderClipShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(20f, 0f)
        path.relativeLineTo(-20f, 20f)
        path.relativeLineTo(0f, size.height - 40f)
        path.relativeLineTo(20f, 20f)
        path.relativeLineTo(size.width / 3f - 20, 0f)
        path.relativeLineTo(15f, -20f)
        path.relativeLineTo(size.width / 3f - 30, 0f)
        path.relativeLineTo(15f, 20f)
        path.relativeLineTo(size.width / 3f - 20, 0f)
        path.relativeLineTo(20f, -20f)
        path.relativeLineTo(0f, -(size.height - 40f))
        path.relativeLineTo(-20f, -20f)
        path.close()

        return Outline.Generic(path)
    }
}
@Composable
fun ArcTextExample() {
    val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
    Canvas(modifier = Modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        drawIntoCanvas {
            Path()
            //AndroidCanvas_androidKt.getNativeCanvas(it).drawText("我她妈的",0,10,100f,100f,paint)
        }
    }
}
