package com.example.composeunit.confing

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

//裁剪圆

@Stable
class CicleImageShape(val circle: Float = 0f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val minWidth = Math.min(size.width - circle, size.width - circle)
        val rect = Rect(circle, circle, minWidth, minWidth)
        val path = Path()
        path.addOval(rect)
        return Outline.Generic(path)
    }
}


@Stable
class AnimalRoundedCornerShape(private val value: Float = 30f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.lineTo(value, 0f)
        path.cubicTo(value, 0f, 0f, 0f, 0f, value)
        path.lineTo(0f, size.height - value)
        path.cubicTo(0f, size.height - value, 0f, size.height, value, size.height)
        path.quadraticBezierTo(size.width / 2, size.height - value, size.width - value, size.height)
        path.quadraticBezierTo(size.width, size.height, size.width, size.height - value)
        path.lineTo(size.width, value)
        path.quadraticBezierTo(size.width, 0f, size.width - value, 0f)
        path.quadraticBezierTo(size.width / 2, value, value, 0f)
        path.lineTo(value, 0f)
        return Outline.Generic(path)
    }

}