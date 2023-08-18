package com.example.composeunit.ui.compose

import androidx.compose.foundation.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*

@Composable
fun myText(text: String) {
    //1.可改变的状态
    val nameState: MutableState<Color> = remember { mutableStateOf(Color.Green) }
    //3.修改状态之后状态向下流动去修改4.中的UI
    Text(
        text = text,
        //`边框`
        modifier = Modifier
            .clickable(
                onClick = {
                    nameState.value = Color.Yellow
                },
            ).background(Color.Gray, shape = RectangleShapes).shadow(elevation = Dp(10f)),
        //`颜色`、
        //4.状态流动下来修改UI显示UI
        color = nameState.value,
        //`字体大小`、
        //字形的厚度
        fontWeight = FontWeight.Bold,
        //斜体
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Default,
        //字体水平间隔
        letterSpacing = TextUnit.Unspecified,
        //在文本上方绘制一条水平线
        textDecoration = TextDecoration.LineThrough,
        fontSize =56.sp
    )
}

@Stable
val RectangleShapes: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val roudRect = RoundRect(10f, 0f, 1110f, 910f, 930f, 160f)
        return Outline.Rounded(roudRect)
    }
}
@Stable
val BoxQureToShapes: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val roundRect= Path()
        roundRect.moveTo(0f,0f)
        roundRect.lineTo(size.width-150f,0f)
        roundRect.quadraticBezierTo(size.width,size.height/2f,size.width-150f,size.height)
        roundRect.lineTo(0f,size.height)
        roundRect.close()
        return Outline.Generic(roundRect)
    }
}