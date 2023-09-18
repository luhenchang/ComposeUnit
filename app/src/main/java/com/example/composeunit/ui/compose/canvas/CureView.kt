package com.example.composeunit.ui.compose.canvas

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.Shader
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeunit.R
import com.example.composeunit.ui.compose.confing.MainActions
import com.example.composeunit.utils.getBitmap

@Preview(name = "canvas")
@Composable
fun MyCureView(mainActions: MainActions, imgList:ArrayList<ImageBitmap>) {
    val imgList:ArrayList<ImageBitmap> = ArrayList()
    imgList.add(getBitmap(resource = R.drawable.head_lhc))
    imgList.add(getBitmap(R.drawable.mn_1))
    imgList.add(getBitmap(R.drawable.mn_2))
    imgList.add(getBitmap(R.drawable.mn_3))
    imgList.add(getBitmap(R.drawable.mn_4))
    imgList.add(getBitmap(R.drawable.mn_5))
    imgList.add(getBitmap(R.drawable.mn_6))
    //距离左边屏幕距离
    val marginToLeft = 180f
    //距离屏幕下边距离
    val marginToBootom = 240f
    //x轴的宽度
    val x_scaleWidth = 0f
    //格子宽搞
    val grid_width = 0f
    val dataList: ArrayList<Int> = ArrayList()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .draggable(state = DraggableState {

            }, orientation = Orientation.Horizontal, onDragStarted =  {
            }, onDragStopped = {
                Log.e("on", "MyCureView: "+this )
            }).wrapContentHeight(),
    ) {
        dataList.add(0)
        dataList.add(0)
        dataList.add(0)
        dataList.add(1400)
        dataList.add(400)
        dataList.add(1350)
        dataList.add(0)


        drawIntoCanvas { canvas ->
            val paint = Paint()
            paint.style = PaintingStyle.Fill
            paint.color = Color.Green
            val textPaint = android.graphics.Paint()
            textPaint.strokeWidth = 2f
            textPaint.style = android.graphics.Paint.Style.FILL
            textPaint.color = android.graphics.Color.BLACK
            textPaint.textSize = 69f
            //1.坐标变换
            canvas.nativeCanvas.drawText(
                "Compose自定义",
                size.width / 3,
                size.height / 2.5f,
                textPaint
            )
            canvas.translate(0f,size.height)
            canvas.scale(1f, -1f)
            //nativeCanvas.translate(0f,size.height)
            //nativeCanvas.scale(1f, -1f)


            canvas.translate(marginToLeft,marginToBootom)
            //nativeCanvas.translate(marginToLeft,marginToBootom)
            //没用的求去掉了 canvas.drawCircle(Offset(100f, 100f), 100f, paint)



            //2.平行x轴线
            drawXLine(marginToLeft, canvas)


            //3.绘制x轴下面的文字
            drawTextDownX(marginToLeft, canvas)

            //4.绘制y轴坐标的文字
            drawTextOfYLeft(marginToLeft, canvas)
            //5.曲线绘制
            drawCubtoCircle(marginToLeft, dataList, canvas)
            //6.美化部分
            drawTextButton(canvas.nativeCanvas)
            //
            drawHeaderToCanvas(canvas.nativeCanvas,size.width,marginToLeft,dataList,imgList)
            //美化结果
            drawResultBitifull(canvas)
        }

    }


}

private fun DrawScope.drawResultBitifull(canvas: androidx.compose.ui.graphics.Canvas) {
    val textPaint = android.graphics.Paint()
    textPaint.strokeWidth = 2f
    textPaint.style = android.graphics.Paint.Style.FILL
    textPaint.color = android.graphics.Color.argb(255, 0, 0, 0)
    textPaint.textSize = 66f
    val rectText = Rect()
    val rectTextYuan = Rect()

    canvas.save()
    canvas.scale(1f, -1f)
    canvas.translate((size.width / 2) - 100, -500f)
    val text = "1347"
    val textyu = "元"

    textPaint.getTextBounds(text, 0, text.length, rectText)

    canvas.nativeCanvas.drawText(
        text,
        -rectText.width().toFloat() - 42f,
        rectText.height().toFloat() / 2,
        textPaint
    )
    textPaint.color = android.graphics.Color.argb(111, 111, 111, 111)
    textPaint.getTextBounds(textyu, 0, textyu.length, rectTextYuan)
    textPaint.textSize = 33f
    canvas.nativeCanvas.drawText(
        textyu,
        80 + -rectTextYuan.width().toFloat() - 42f,
        rectTextYuan.height().toFloat() / 2,
        textPaint
    )

    canvas.translate(0f, 50f)
    canvas.nativeCanvas.drawText(
        "较前天",
        -rectTextYuan.width().toFloat() - 180f,
        rectTextYuan.height().toFloat() / 2,
        textPaint
    )
    canvas.translate(100f, 0f)
    textPaint.color = android.graphics.Color.argb(255, 223, 129, 120)
    canvas.nativeCanvas.drawText(
        "+971.99(251.19%)",
        -rectTextYuan.width().toFloat() - 180f,
        rectTextYuan.height().toFloat() / 2,
        textPaint
    )
    canvas.translate(-100f, 50f)
    textPaint.color = android.graphics.Color.argb(111, 111, 111, 111)
    canvas.nativeCanvas.drawText(
        "对应图中虚线部分进行最高评奖",
        -rectTextYuan.width().toFloat() - 180f,
        rectTextYuan.height().toFloat() / 2,
        textPaint
    )
    //暂时没找到canvas绘制富文本的方法。只能一个个测量绘制文字了。别学我,好好测量测量有待提高自己的小学计算。

    canvas.restore()
}

//8.绘制每天最高获得者的头像...纯虚构故事对不对..
private fun drawHeaderToCanvas(canvas: Canvas,width:Float,marginToLeft:Float,dataList:List<Int>,imgList:ArrayList<ImageBitmap>) {
    val bitmapPaint = android.graphics.Paint()
    bitmapPaint.strokeWidth = 2f
    bitmapPaint.style = android.graphics.Paint.Style.STROKE
    bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    bitmapPaint.isAntiAlias =true
    canvas.save()
    val srcRect1=Rect(0, 0, 80, 80)
    val dstRect1=Rect(0, 0, 40, 40)
    val x_scaleWidth = (width - marginToLeft - 80f)
    val grid_width = x_scaleWidth / 6
    val danweiY = (grid_width - 40) / 500
    for (index in 0 until dataList.size) {
        val bitmap = imgList[index].asAndroidBitmap()
        canvas.save()
        canvas.translate(
            grid_width * index - bitmap.width /20,
            danweiY * dataList[index] + 20
        )
        //这里绘制图片到画布上
        val circlePath = Path()
        circlePath.addCircle(20f,20f, 20f, Path.Direction.CCW)
        canvas.clipPath(circlePath)
        canvas.drawBitmap(bitmap, srcRect1, dstRect1, bitmapPaint)
        canvas.restore()
    }
    canvas.restore()


}

@SuppressLint("ObsoleteSdkInt")
fun drawTextButton(canvas: Canvas) {
    val linePaint = android.graphics.Paint()
    linePaint.strokeWidth = 2f
    linePaint.style = android.graphics.Paint.Style.STROKE
    linePaint.color = android.graphics.Color.argb(188, 76, 126, 245)
    linePaint.textSize=32f
    val buttonPath = Path()
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        buttonPath.addRoundRect(110f, -120f, 270f, -180f, 80f, 80f, Path.Direction.CCW)
    }
    canvas.drawPath(buttonPath, linePaint)
    canvas.save()
    canvas.scale(1f, -1f)
    linePaint.style = android.graphics.Paint.Style.FILL
    canvas.drawText("前 七 天", 140f, 165f, linePaint)
    canvas.restore()

    canvas.save()
    canvas.translate(260f, 0f)
    linePaint.style = android.graphics.Paint.Style.STROKE
    canvas.drawPath(buttonPath, linePaint)
    canvas.scale(1f, -1f)
    linePaint.style = android.graphics.Paint.Style.FILL
    canvas.drawText("后 七 天", 140f, 165f, linePaint)
    canvas.restore()

}

private fun DrawScope.drawCubtoCircle(
    marginToLeft: Float,
    dataList: ArrayList<Int>,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    val gridWidth1: Float
    val xScaleWidth1: Float = (size.width - marginToLeft - 80f)
    gridWidth1 = xScaleWidth1 / 6
    val textPaint = android.graphics.Paint()
    textPaint.strokeWidth = 2f
    textPaint.style = android.graphics.Paint.Style.FILL
    textPaint.color = android.graphics.Color.argb(100, 111, 111, 111)

    val cavesPath = Path()
    //500=grid_width-40 每个单位的长度的=像素长度
    val danweiY = (gridWidth1 - 40) / 500
    val danweiX = (gridWidth1)
    val linearGradient = LinearGradient(
        0f, 1500 * danweiY,
        0f,
        0f,
        android.graphics.Color.argb(255, 229, 160, 144),
        android.graphics.Color.argb(255, 251, 244, 240),
        Shader.TileMode.CLAMP
    )
    textPaint.shader = linearGradient
    for (index in 0 until dataList.size - 1) {
        val xMoveDistance = 20
        val yMoveDistance = 40

        if (dataList[index] == dataList[index + 1]) {
            cavesPath.lineTo(danweiX * (index + 1), 0f)
        } else if (dataList[index] < dataList[index + 1]) {//y1<y2情况
            val centerX = (gridWidth1 * index + gridWidth1 * (1 + index)) / 2
            val centerY =
                (dataList[index].toFloat() * danweiY + dataList[index + 1].toFloat() * danweiY) / 2
            val controX0 = (gridWidth1 * index + centerX) / 2
            val controY0 = (dataList[index].toFloat() * danweiY + centerY) / 2
            val controX1 = (centerX + gridWidth1 * (1 + index)) / 2
            val controY1 = (centerY + dataList[index + 1].toFloat() * danweiY) / 2
            cavesPath.cubicTo(
                controX0 + xMoveDistance,
                controY0 - yMoveDistance,
                controX1 - xMoveDistance,
                controY1 + yMoveDistance,
                gridWidth1 * (1 + index),
                dataList[index + 1].toFloat() * danweiY
            )
        } else {
            val centerX = (gridWidth1 * index + gridWidth1 * (1 + index)) / 2
            val centerY =
                (dataList[index].toFloat() * danweiY + dataList[index + 1].toFloat() * danweiY) / 2
            val controX0 = (gridWidth1 * index + centerX) / 2
            val controY0 = (dataList[index].toFloat() * danweiY + centerY) / 2
            val controX1 = (centerX + gridWidth1 * (1 + index)) / 2
            val controY1 = (centerY + dataList[index + 1].toFloat() * danweiY) / 2
            cavesPath.cubicTo(
                controX0 + xMoveDistance,
                controY0 + yMoveDistance,
                controX1 - xMoveDistance,
                controY1 - yMoveDistance,
                gridWidth1 * (1 + index),
                dataList[index + 1].toFloat() * danweiY
            )

        }
    }
    canvas.nativeCanvas.drawCircle(0f, 0f, 10f, textPaint)
    //绘制闭合渐变曲线
    canvas.nativeCanvas.drawPath(cavesPath, textPaint)
    val linePaint = android.graphics.Paint()
    linePaint.strokeWidth = 3f
    linePaint.style = android.graphics.Paint.Style.STROKE
    linePaint.color = android.graphics.Color.argb(255, 212, 100, 77)
    //绘制外环红色线
    canvas.nativeCanvas.drawPath(cavesPath, linePaint)
    linePaint.style = android.graphics.Paint.Style.FILL
    //画圈。
    for (index in 0 until dataList.size) {
        canvas.nativeCanvas.drawCircle(
            gridWidth1 * index,
            danweiY * dataList[index],
            8f,
            linePaint
        )
    }
}


private fun DrawScope.drawTextOfYLeft(
    marginToLeft: Float,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    val textPaint = android.graphics.Paint()
    textPaint.strokeWidth = 2f
    textPaint.style = android.graphics.Paint.Style.STROKE
    textPaint.color = android.graphics.Color.argb(100, 111, 111, 111)
    textPaint.textSize = 19f

    val xScaleWidth1: Float = (size.width - marginToLeft - 80f)
    val gridWidth1: Float = xScaleWidth1 / 6

    val rectText = Rect()
    canvas.save()
    //将文字旋转摆正，此时坐标系y向下是正
    (0 until 4).forEach { index ->
        if (index > 0) {
            canvas.translate(0f, gridWidth1 - 40f)
        }
        var strTx = ""
        strTx = when (index) {
            0 -> {
                "$index"
            }
            1 -> {
                "${500}"
            }
            2 -> {
                "1k"
            }
            else -> {
                "1.5k"
            }
        }

        canvas.save()
        canvas.scale(1f, -1f)
        textPaint.getTextBounds(strTx, 0, strTx.length, rectText)
        canvas.nativeCanvas.drawText(
            strTx,
            -rectText.width().toFloat() - 42f,
            rectText.height().toFloat() / 2,
            textPaint
        )
        canvas.restore()
    }
    canvas.restore()
}

fun DrawScope.drawTextDownX(
    marginToLeft: Float,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    val gridWidth1: Float
    val xScaleWidth1: Float = (size.width - marginToLeft - 80f)
    gridWidth1 = xScaleWidth1 / 6
    val textPaint = android.graphics.Paint()
    textPaint.strokeWidth = 2f
    textPaint.style = android.graphics.Paint.Style.STROKE
    textPaint.color = android.graphics.Color.argb(100, 111, 111, 111)
    textPaint.textSize = 19f
    val rectText = Rect()
    canvas.save()
    //将文字旋转摆正，此时坐标系y向下是正
    canvas.scale(1f, -1f)
    (0 until 7).forEach { index ->
        if (index > 0) {
            Log.e("weima?", "MyCureView: $gridWidth1")
            canvas.nativeCanvas.translate(gridWidth1, 0f)
        }
        val strTx = "11.${11 + index}"
        textPaint.getTextBounds(strTx, 0, strTx.length, rectText)
        canvas.nativeCanvas.drawText(
            strTx,
            -rectText.width().toFloat() / 2,
            rectText.height().toFloat() * 2.5f,
            textPaint
        )
    }
    canvas.restore()
}

private fun DrawScope.drawXLine(
    marginToLeft: Float,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    val linePaint = Paint()
    linePaint.strokeWidth = 2f
    linePaint.style = PaintingStyle.Stroke
    linePaint.color = Color(188, 188, 188, 100)
    //x轴距离右边也留80距离
    val xScaleWidth1: Float = (size.width - marginToLeft - 80f)
    val gridWidth1: Float = xScaleWidth1 / 6

    val onePath = androidx.compose.ui.graphics.Path()
    onePath.lineTo(xScaleWidth1, 0f)
    canvas.drawPath(onePath, linePaint)

    canvas.save()
    //通过平移画布绘制剩余的平行x轴线
    (0 until 3).forEach { _ ->
        canvas.translate(0f, gridWidth1 - 40f)
        canvas.drawPath(onePath, linePaint)
    }
    canvas.restore()

}


