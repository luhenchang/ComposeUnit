package com.example.composeunit.ui.compose.home.shape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeunit.ui.navigation.BottomBarScreen

//背景动画
@Composable
fun BottomBarAnimalBgView(
    radius: Float = 60f,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val paintColor = MaterialTheme.colors.primary.copy(alpha = 0.6f)
    val indexValue = when (currentDestination?.route) {
        BottomBarScreen.Home.route ->  0
        BottomBarScreen.Widget.argumentRoute() ->  1
        BottomBarScreen.Setting.route -> 2
        else -> {0}
    }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clickable(enabled = false) {},
        onDraw = {
            drawIntoCanvas { canvas ->
                val paint = Paint()
                paint.color = paintColor
                paint.style = PaintingStyle.Fill
                //先固定分为三等分
                val widthOfOne = size.width / 3
                //每一个弧度的中心控制点
                val centerWidthOfOneX = widthOfOne / 2
                //弧度端口到两遍ONewidth距离
                val marginLeftAndRight = centerWidthOfOneX / 1.6f

                val controllerX = centerWidthOfOneX / 6f

                val keyAnimal = widthOfOne * indexValue
                canvas.save()
                paint.asFrameworkPaint().setShadowLayer(
                    15f,
                    5f,
                    -6f,
                    Color.White.toArgb()
                )
                canvas.drawCircle(Offset(centerWidthOfOneX + keyAnimal, 0f), radius, paint)

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(marginLeftAndRight / 2 + keyAnimal, 0f)
                    cubicTo(
                        marginLeftAndRight + keyAnimal,
                        0f,
                        centerWidthOfOneX - (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 3f,
                        centerWidthOfOneX + keyAnimal,
                        size.height / 2.6f
                    )
                    cubicTo(
                        centerWidthOfOneX + (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 2.6f,
                        widthOfOne - (marginLeftAndRight) + keyAnimal,
                        0f,
                        widthOfOne - marginLeftAndRight / 2 + keyAnimal,
                        0f
                    )
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height + 30)
                    lineTo(0f, size.height + 30)
                    close()
                }

                canvas.drawPath(path, paint)

                //裁剪
                val clipPath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(marginLeftAndRight / 2 + keyAnimal, 0f)
                    cubicTo(
                        marginLeftAndRight + keyAnimal,
                        0f,
                        centerWidthOfOneX - (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 3f,
                        centerWidthOfOneX + keyAnimal,
                        size.height / 2.6f
                    )
                    cubicTo(
                        centerWidthOfOneX + (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
                        size.height / 2.6f,
                        widthOfOne - (marginLeftAndRight) + keyAnimal,
                        0f,
                        widthOfOne - marginLeftAndRight / 2 + keyAnimal,
                        0f
                    )
                    lineTo(size.width, 0f)
                    close()
                }
                canvas.clipPath(clipPath, ClipOp.Difference)
            }
        })
}


class BottomShape(indexValue: Float) : Shape {
    private val indexValue = indexValue
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        //先固定分为三等分
        val widthOfOne = size.width / 3
        //每一个弧度的中心控制点
        val centerWidthOfOneX = widthOfOne / 2
        //弧度端口到两遍ONewidth距离
        val marginLeftAndRight = centerWidthOfOneX / 1.6f

        val controllerX = centerWidthOfOneX / 6f
        val keyAnimal = widthOfOne * indexValue

        val clipPath = Path()
        clipPath.moveTo(0f, 0f)
        clipPath.lineTo(marginLeftAndRight / 2 + keyAnimal, 0f)
        clipPath.cubicTo(
            marginLeftAndRight + keyAnimal,
            0f,
            centerWidthOfOneX - (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
            size.height / 3f,
            centerWidthOfOneX + keyAnimal,
            size.height / 2.6f
        )
        clipPath.cubicTo(
            centerWidthOfOneX + (centerWidthOfOneX - controllerX) / 2f + keyAnimal,
            size.height / 2.6f,
            widthOfOne - (marginLeftAndRight) + keyAnimal,
            0f,
            widthOfOne - marginLeftAndRight / 2 + keyAnimal,
            0f
        )
        clipPath.lineTo(size.width, 0f)
        clipPath.lineTo(size.width, size.height)
        clipPath.lineTo(0f, size.height)
        clipPath.close()
        return Outline.Generic(clipPath)
    }

}