package com.example.composeunit.ui.compose.login
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Shader
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeunit.utils.getBitmap
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.composeunit.R
import com.example.composeunit.ui.compose.other.confing.AnimalRoundedCornerShape
import com.example.composeunit.ui.compose.other.confing.CicleImageShape
import com.example.composeunit.ui.compose.other.confing.MainActions
import com.example.composeunit.ui.compose.other.canvas.QureytoImageShapes
import com.example.composeunit.utils.BitmapBlur

/**
 * @param
 * 登陆页面的底部模糊缩放背景
 */

@Composable
fun LoginPageBackgroundBlurImage(
    animatedRound: Animatable<Float, AnimationVector1D>,
    animatedScales: Animatable<Float, AnimationVector1D>
) {
    Image(
        bitmap = BitmapBlur.doBlur(
            getBitmap(resource = R.drawable.head_god).asAndroidBitmap(),
            animatedRound.value.toInt() + 20, false
        ).asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .scale(animatedScales.value, animatedScales.value),
    )
}

/**
 * 登陆背景模糊头缩放部图片
 */
@Composable
fun LoginPageTopBlurImage(
    animatedBitmap: Animatable<Float, AnimationVector1D>,
    animatedOffset: Animatable<Float, AnimationVector1D>,
    animatedScales: Animatable<Float, AnimationVector1D>
) {
    Image(
        bitmap = BitmapBlur.doBlur(
            getBitmap(resource = R.drawable.head_god).asAndroidBitmap(),
            animatedBitmap.value.toInt(), false
        ).asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(
                QureytoImageShapes(160f, animatedOffset.value)
            )
            .scale(animatedScales.value, animatedScales.value)
    )
}

/**
 * 登陆页面头部旋转缩放的图片
 */
@Composable
fun LoginPageTopRotaAndScaleImage(
    animatedColor: Animatable<androidx.compose.ui.graphics.Color, AnimationVector4D>,
    animatedScales: Animatable<Float, AnimationVector1D>,
    animatedOffset: Animatable<Float, AnimationVector1D>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp)
            .clip(CicleImageShape())
            .background(animatedColor.value)
            .width((130 * animatedScales.value).dp)
            .height((130 * animatedScales.value).dp)
    ) {
        Image(
            bitmap = getBitmap(R.drawable.head_god),
            contentDescription = "w",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .background(color = Color(0XFF0DBEBF), shape = CircleShape)
                .padding(3.dp)
                .clip(
                    CircleShape
                )
                .shadow(elevation = 150.dp, clip = true)
                .rotate(
                    animatedOffset.value
                )
        )
    }
}

/**
 * 登陆页面顶部文字
 */
@Composable
fun LoginPageTopTextBox(
    animatedOffset: Animatable<Float, AnimationVector1D>,
    animatedScales: Animatable<Float, AnimationVector1D>
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .draggable(state = DraggableState {

                    }, orientation = Orientation.Horizontal, onDragStarted = {
                    }, onDragStopped = {

                    }),
            ) {
                drawIntoCanvas { canvas ->
                    val paint = androidx.compose.ui.graphics.Paint()
                    paint.style = PaintingStyle.Fill
                    paint.color = androidx.compose.ui.graphics.Color.Green
                    val textPaint = Paint()
                    textPaint.strokeWidth = 2f
                    textPaint.style = Paint.Style.FILL
                    textPaint.color = Color.BLACK
                    textPaint.textSize = 52f


                    //测量文字宽度
                    val rect = Rect()
                    textPaint.getTextBounds("ComposeUnit 登陆", 0, 6, rect)
                    val colors = intArrayOf(
                        Color.BLACK,
                        Color.argb(
                            250,
                            121,
                            animatedOffset.value.toInt(),
                            206
                        ),
                        Color.argb(
                            250,
                            121,
                            206,
                            animatedOffset.value.toInt()
                        )
                    )
                    val positions = floatArrayOf(0.2f, 11f, 0.2f)


                    //让渐变动起来从而感觉到文字闪动起来了
                    val transMatrix = Matrix()
                    transMatrix.postTranslate(
                        -rect.width() + rect.width() * 2 * (animatedScales.value * 1.5f),
                        0f
                    )
                    //设置渐变
                    val linearGradient = LinearGradient(
                        0f,
                        0f,
                        rect.width().toFloat(),
                        0f,
                        colors,
                        positions,
                        Shader.TileMode.CLAMP
                    )
                    //设置矩阵变换
                    linearGradient.setLocalMatrix(transMatrix)

                    textPaint.shader = linearGradient
                    //1.坐标变换
                    canvas.nativeCanvas.drawText(
                        "ComposeUnit 登陆",
                        size.width / 3.5f,
                        size.height / 2.5f,
                        textPaint
                    )
                    val secontextPath = navePath()


                    val rect1 = Rect()
                    textPaint.getTextBounds("更多精彩,更多体验 ~", 0, 6, rect1)
                    secontextPath.moveTo(340f, 100f)
                    //0-110
                    if (animatedOffset.value == 0f) {
                        secontextPath.quadTo(350f, 10f, 710f, 100f)

                    }
                    secontextPath.quadTo(animatedOffset.value, 10f, 710f, 100f)

                    textPaint.textSize = 32f
                    textPaint.letterSpacing = 0.3f
                    //canvas.nativeCanvas.drawPath(secontextPath,textPaint)
                    canvas.nativeCanvas.drawTextOnPath(
                        "更多精彩,更多体验 ~",
                        secontextPath,
                        0f,
                        0f,
                        textPaint
                    )
                }

            }
        }
    }
}

/**
 * 登陆页面输入框
 */
@Composable
fun LoginPageInput(
    inputUserName: MutableState<String>,
    animatedColor: Animatable<androidx.compose.ui.graphics.Color, AnimationVector4D>,
    animatedRound: Animatable<Float, AnimationVector1D>,
    inputPassworld: MutableState<String>
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp)
    ) {
        TextField(
            value = inputUserName.value,
            onValueChange = {
                inputUserName.value = it.trim()
            },
            // shape = AnimalRoundedCornerShape(animatedRound.value),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                backgroundColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            modifier = Modifier
                .height(48.dp)
                .border(
                    1.2.dp,
                    //animatedColor.value.copy(alpha = 1f)
                    Color(
                        animatedColor.value.red,
                        animatedColor.value.green,
                        animatedColor.value.blue,
                        1f
                    ),
                    //shape = RoundedCornerShape(18.dp)
                    AnimalRoundedCornerShape(animatedRound.value)
                ),
            leadingIcon = {
                Icon(
                    bitmap = getBitmap(R.mipmap.yinzhang),
                    contentDescription = ""
                )
            })
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        TextField(
            value = inputPassworld.value,
            readOnly = false,
            onValueChange = {
                inputPassworld.value = it.trim()
            },
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                backgroundColor = androidx.compose.ui.graphics.Color.Transparent

            ),
            modifier = Modifier
                .height(48.dp)
                .border(
                    1.2.dp,
                    Color(
                        animatedColor.value.red,
                        animatedColor.value.green,
                        animatedColor.value.blue,
                        1f
                    ),
                    AnimalRoundedCornerShape(animatedRound.value)
                ),
            textStyle = TextStyle(),
            leadingIcon = {
                Icon(
                    bitmap = getBitmap(R.mipmap.suozi),
                    contentDescription = ""
                )
            },
            trailingIcon = {
                Icon(
                    bitmap = getBitmap(R.mipmap.yanjing),
                    contentDescription = ""
                )
            })

    }
}

/**
 * 用户选择CheckBox
 */
@Composable
fun LoginPageCheckBox(
    animatedCheckBox: Animatable<Float, AnimationVector1D>,
    animatedOffset: Animatable<Float, AnimationVector1D>,
    animatedRound: Animatable<Float, AnimationVector1D>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 20.dp)
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = { },
                colors = CheckboxDefaults.colors(checkedColor = Color(0XFF0DBEBF)),
                modifier = Modifier.clip(CicleImageShape(animatedCheckBox.value))
            )
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, top = 20.dp)
                    .draggable(state = DraggableState {

                    }, orientation = Orientation.Horizontal, onDragStarted = {
                    }, onDragStopped = {
                        Log.e("on", "MyCureView: $this")
                    }),
            ) {
                drawIntoCanvas { canvas ->
                    val paint = androidx.compose.ui.graphics.Paint()
                    paint.style = PaintingStyle.Fill
                    paint.color = androidx.compose.ui.graphics.Color.Green
                    val textPaint = Paint()
                    textPaint.strokeWidth = 2f
                    textPaint.style = Paint.Style.FILL
                    textPaint.color = Color(0XFF0DBEBF).toArgb()
                    textPaint.textSize = 52f


                    val secontextPath = Path()
                    val rect1 = Rect()
                    textPaint.getTextBounds("用户注册", 0, 4, rect1)
                    secontextPath.moveTo(300f, 0f)
                    //0-110
                    if (animatedOffset.value < 11f) {
                        secontextPath.quadTo(350f, 0f, 530f, 0f)
                    } else {
                        secontextPath.quadTo(350f, animatedRound.value, 530f, 0f)
                    }
                    textPaint.letterSpacing = 0.3f
                    textPaint.style = Paint.Style.STROKE
                    canvas.nativeCanvas.drawPath(secontextPath, textPaint)
                    textPaint.style = Paint.Style.FILL
                    canvas.nativeCanvas.drawTextOnPath(
                        "用户注册",
                        secontextPath,
                        0f,
                        0f,
                        textPaint
                    )
                }
            }
        }

    }
}

/**
 * 登陆页面底部按钮
 */
@Composable
fun LoginPageBootomButton(
    animatedScales: Animatable<Float, AnimationVector1D>,
    animatedColor: Animatable<androidx.compose.ui.graphics.Color, AnimationVector4D>,
    mainActions: MainActions,
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.login_other_login),
                fontSize = 16.sp,
                color = Color(0XFF0DBEBF),
                modifier = Modifier.scale(animatedScales.value).shadow(10.dp)
            )
            Image(
                bitmap = getBitmap(R.mipmap.github),
                contentDescription = "w",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .background(
                        color = animatedColor.value.copy(alpha = 1f),
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clip(
                        CircleShape
                    )
                    .shadow(elevation = 150.dp, clip = true)
                    .clickable(onClick = {
                        mainActions.homePage()
                    })
            )
        }
    }
}

fun navePath(): Path {
    return Path()
}
