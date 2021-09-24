package com.example.composeunit.project.fragment

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeunit.R
import com.example.composeunit.confing.CicleImageShape
import com.example.composeunit.confing.MainActions
import com.example.composeunit.project.view_model.message.MessageViewModel
import com.example.composeunit.utils.getBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TwoFragment(mainActions: MainActions, viewModel: MessageViewModel = viewModel()) {
    MessagePageCanvasQueryBox(mainActions, viewModel)
}

@ExperimentalFoundationApi
@Composable
fun MessagePageCanvasQueryBox(
    mainActions: MainActions,
    viewModel: MessageViewModel
) {
    val uiData = ArrayList<Int>()
    uiData.add(R.drawable.android_icon)
    uiData.add(R.drawable.android_5g)
    uiData.add(R.drawable.android_bgicon)
    uiData.add(R.drawable.android_clock)
    uiData.add(R.drawable.android_kotlin)

    val sizeAnimal: Float by animateFloatAsState(
        if (viewModel.animalChange.value) {
            0f
        } else {
            300f
        }, animationSpec = TweenSpec(durationMillis = 100)
    )
    //拖拽事件记录x和y坐标
    val offsetX = remember { mutableStateOf(120f) }
    val offsetY = remember { mutableStateOf(120f) }
    val width = remember { mutableStateOf(1000f) }
    val height = remember { mutableStateOf(1920f) }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier
        .pointerInput(Unit) {
            coroutineScope {
                while (true) {
                    val offset = awaitPointerEventScope {
                        awaitFirstDown().position
                    }
                    launch {
                        viewModel.animatedOffsetY.animateTo(
                            offset.y,
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        )
                    }

                }
            }
        }
        .background(Color(238, 239, 247, 255))) {
        Box() {
            Box(
            ) {
                Column(
                    Modifier
                        .padding(top = 180.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Compose UI",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(start = 20.dp, bottom = 5.dp)
                    )
                    val scrollLazyState = rememberLazyListState()
                    LazyRow(
                        state = scrollLazyState,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        items(uiData.size) { index ->
                            TopListItem(mainActions, uiData.get(index))
                        }
                    }
                    //val scrollVerticleLazyState = rememberLazyListState()
                    Text(
                        text = "Compose Canvas",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(start = 20.dp, bottom = 0.dp, top = 15.dp)
                    )
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 5.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        items(14) {
                            BootomItem()
                        }
                    }
                }
                TopCanvasBitiful(viewModel)
            }
            Box {
                Box(
                    modifier = Modifier.animalModifier(viewModel.animalChange.value, sizeAnimal)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        onDraw = {
                            drawIntoCanvas { canvas ->
                                val paint = Paint()
                                paint.style = Paint.Style.FILL
                                paint.shader = LinearGradientShader(
                                    Offset(0f, 0f),
                                    Offset(size.width / 2, 0f),
                                    arrayListOf(Color(0xFF024D4D), Color(0xFF1AA8A8)),
                                    arrayListOf(0.5f, 1f)
                                )
                                paint.isAntiAlias = true
                                paint.strokeWidth = 10f
                                val roundRect = Path()
                                roundRect.moveTo(0f, 0f)
                                roundRect.lineTo(size.width / 2.5f, 0f)
                                val controllerX = size.width / 1.4f
                                val controllerEnd = size.width / 2.5f
                                if (viewModel.animatedOffsetY.value == 0f) {
                                    roundRect.quadTo(
                                        controllerX,
                                        size.height / 2f,
                                        controllerEnd,
                                        size.height
                                    )
                                } else {
                                    roundRect.quadTo(
                                        controllerX,
                                        viewModel.animatedOffsetY.value,
                                        controllerEnd,
                                        size.height
                                    )
                                }
                                roundRect.lineTo(0f, size.height)
                                roundRect.close()
                                //canvas.nativeCanvas.clipPath(roundRect)
                                val roundOutQueToPath = Path()
                                roundOutQueToPath.lineTo(size.width / 2.5f, 0f)
                                if (viewModel.animatedOffsetY.value == 0f) {
                                    roundOutQueToPath.quadTo(
                                        controllerX,
                                        size.height / 2f,
                                        controllerEnd,
                                        size.height
                                    )
                                } else {
                                    roundOutQueToPath.quadTo(
                                        controllerX,
                                        viewModel.animatedOffsetY.value,
                                        controllerEnd,
                                        size.height
                                    )
                                }
                                roundOutQueToPath.lineTo(
                                    controllerEnd,
                                    size.height
                                )
                                roundOutQueToPath.lineTo(0f, size.height)
                                paint.setShadowLayer(
                                    15f,
                                    5f,
                                    5f,
                                    android.graphics.Color.argb(255, 130, 180, 206)
                                )
                                canvas.nativeCanvas.drawPath(roundOutQueToPath, paint)

                            }
                        }
                    )
                }
                DrawLayout(offsetX, offsetY, viewModel, width, height, sizeAnimal)
            }
        }


    }
}


/**
 * 顶部好看的云布局
 * @param viewModel
 */
@Composable
private fun TopCanvasBitiful(viewModel: MessageViewModel) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(),
        onDraw = {
            drawIntoCanvas { canvas ->
                //第一个弧度内容
                val paint = Paint()
                paint.style = Paint.Style.FILL
                paint.shader = LinearGradientShader(
                    Offset(0f, 0f),
                    Offset(size.width / 2, 0f),
                    arrayListOf(Color(227, 199, 169, 255), Color(227, 199, 169, 255)),
                    arrayListOf(0.5f, 1f)
                )
                paint.isAntiAlias = true
                paint.strokeWidth = 10f
                paint.setShadowLayer(
                    25f,
                    15f,
                    15f,
                    android.graphics.Color.argb(255, 130, 180, 206)
                )
                val roundRect = Path()
                roundRect.moveTo(0f, 0f)
                roundRect.lineTo(size.width / 3.3f, 0f)
                val controllerOne = size.width / 2.3f
                roundRect.cubicTo(controllerOne, 200f, 200f, 200f, 0f, 250f)
                roundRect.lineTo(0f, 250f)
                roundRect.close()
                canvas.nativeCanvas.drawPath(roundRect, paint)

                //第二个弧度内容
                val roundRect1 = Path()
                roundRect1.moveTo(size.width / 1.5f, 0f)
                roundRect1.lineTo(size.width / 3.3f, 0f)
                val controllerTwo = size.width / 2.3f
                roundRect1.cubicTo(controllerTwo, 200f, 200f, 200f, 0f, 250f)
                roundRect1.lineTo(0f, 250f)
                roundRect1.lineTo(0f, 360f)
                roundRect1.cubicTo(
                    380f,
                    450f,
                    size.width / 1.5f,
                    320f,
                    size.width / 2.3f,
                    200f
                )
                roundRect1.cubicTo(
                    size.width / 2.8f,
                    130f,
                    size.width / 1.2f,
                    80f,
                    size.width / 1.2f,
                    0f
                )
                roundRect1.close()
                paint.shader = LinearGradientShader(
                    Offset(0f, 0f),
                    Offset(size.width / 2, 0f),
                    arrayListOf(Color(166, 199, 225, 255), Color(166, 199, 225, 255)),
                    arrayListOf(0.5f, 1f)
                )
                canvas.nativeCanvas.drawPath(roundRect1, paint)
                //第三个弧度内容
                val roundRect2 = Path()
                roundRect2.moveTo(size.width / 1.2f + 80f, 0f)
                roundRect2.cubicTo(
                    size.width / 1.2f,
                    200f,
                    size.width / 1.2f - 10f,
                    460f,
                    size.width / 2.3f,
                    200f
                )
                roundRect2.cubicTo(
                    size.width / 2.8f,
                    130f,
                    size.width / 1.2f,
                    80f,
                    size.width / 1.2f,
                    0f
                )

                //roundRect2.cubicTo(size.width/1.2f,200f,size.width/1.5f-10f,300f,size.width/2.3f,200f)

                roundRect2.close()
                paint.shader = LinearGradientShader(
                    Offset(0f, 0f),
                    Offset(size.width / 2, 0f),
                    arrayListOf(Color(133, 130, 206, 255), Color(133, 130, 206, 255)),
                    arrayListOf(0.5f, 1f)
                )
                canvas.nativeCanvas.drawPath(roundRect2, paint)


                //第四个弧度内容
                val roundRect3 = Path()
                roundRect3.moveTo(size.width / 1.2f + 80f, 0f)
                roundRect3.cubicTo(
                    size.width / 1.2f,
                    200f,
                    size.width / 1.2f - 10f,
                    460f,
                    size.width / 2.3f,
                    200f
                )
                roundRect3.cubicTo(
                    size.width / 1.6f,
                    310f,
                    size.width / 2.35f,
                    360f,
                    size.width / 2.8f,
                    379f
                )
                roundRect3.cubicTo(
                    viewModel.animatedOffsetY.value,
                    520f,
                    size.width,
                    400f,
                    size.width,
                    250f
                )
                roundRect3.lineTo(size.width, 250f)
                roundRect3.lineTo(size.width, 0f)
                roundRect3.close()
                paint.shader = LinearGradientShader(
                    Offset(0f, 0f),
                    Offset(size.width / 2, 0f),
                    arrayListOf(Color(226, 176, 174, 255), Color(226, 176, 174, 255)),
                    arrayListOf(0.5f, 1f)
                )
                canvas.nativeCanvas.drawPath(roundRect3, paint)
            }
        }
    )
}


/**
 * 侧面的弧度部分布局
 * @param offsetX
 * @param offsetY
 * @param viewModel
 * @param width
 * @param height
 * @param sizeAnimal
 */
@Composable
private fun DrawLayout(
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    viewModel: MessageViewModel,
    width: MutableState<Float>,
    height: MutableState<Float>,
    sizeAnimal: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = getBitmap(R.drawable.head_god),
            contentDescription = "w",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
                .offset {
                    IntOffset(
                        offsetX.value.roundToInt(),
                        offsetY.value.roundToInt()
                    )
                }
                .background(color = Color(0XFF0DBEBF), shape = CircleShape)
                .padding(3.dp)
                .clip(
                    CircleShape
                )
                .shadow(elevation = 150.dp, clip = true)
                .rotate(
                    viewModel.animatedOffset.value
                )
                .clickable {
                    //如果出现侧滑曲线我们进行复位头像
                    if (viewModel.animalChange.value) {
                        offsetX.value = 120f
                        offsetY.value = 120f
                    }
                    //是否现实测面曲线
                    viewModel.animalChange.value = !viewModel.animalChange.value
                }
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            var change =
                                awaitTouchSlopOrCancellation(down.id) { change, over ->
                                    val original = Offset(offsetX.value, offsetY.value)
                                    val summed = original + over
                                    val newValue = Offset(
                                        x = summed.x.coerceIn(
                                            0f,
                                            width.value - 10.dp.toPx()
                                        ),
                                        y = summed.y.coerceIn(
                                            0f,
                                            height.value - 150.dp.toPx()
                                        )
                                    )
                                    change.consumePositionChange()
                                    offsetX.value = newValue.x
                                    offsetY.value = newValue.y
                                }
                            while (change != null && change.pressed) {
                                change = awaitDragOrCancellation(change.id)
                                if (change != null && change.pressed) {
                                    val original = Offset(offsetX.value, offsetY.value)
                                    val summed = original + change.positionChange()
                                    val newValue = Offset(
                                        x = summed.x.coerceIn(
                                            0f,
                                            width.value - 10.dp.toPx()
                                        ),
                                        y = summed.y.coerceIn(
                                            0f,
                                            height.value - 150.dp.toPx()
                                        )
                                    )
                                    change.consumePositionChange()
                                    offsetX.value = newValue.x
                                    offsetY.value = newValue.y
                                }
                            }
                        }
                    }
                }
        )

        Column(
            modifier = Modifier
                .offset(x = 40.dp, y = 50.dp)
                .animalModifier(viewModel.animalChange.value, sizeAnimal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hello_World",
                fontSize = 13.sp,
                color = Color.White
            )
            Text(
                text = "路很长一加油",
                fontSize = 8.sp,
                color = Color.White
            )

            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }

            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }



            Row(modifier = Modifier.padding(top = 95.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
            Row(modifier = Modifier.padding(top = 45.dp)) {
                Image(
                    bitmap = getBitmap(resource = R.drawable.home),
                    contentDescription = "1",
                    modifier = Modifier
                        .clickable {
                        }
                        .padding(end = 15.dp)
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

    }
}
















/**
 * 底部的Item
 */
@Composable
private fun BootomItem() {
    val colorArray = arrayListOf(
        Color(227, 199, 169, 255),
        Color(166, 199, 225, 255),
        Color(133, 130, 206, 255),
        Color(226, 176, 174, 255),
        Color(162, 192, 206, 255)
    )
    val random = (0..4).random()
    Box(contentAlignment = Alignment.BottomCenter) {
        Box(Modifier.padding(top = 10.dp, bottom = 25.dp)) {
            Column(
                Modifier
                    .width(125.dp)
                    .height(150.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        colorArray[random],
                        RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(75.dp)
                        .height(90.dp),
                    bitmap = getBitmap(R.drawable.jetpack),
                    contentDescription = "1"
                )

                Text(
                    text = "首页",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }
        Box(contentAlignment = Alignment.Center) {
            DrawBackCircleAndShaper(colorShadow = colorArray[random].toArgb())
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "5620", fontSize = 11.sp, modifier = Modifier
                        .width(40.dp)
                        .padding(bottom = 16.dp, start = 10.dp)
                )
                Column {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clip(CicleImageShape(20f))
                            .shadow(elevation = 15.dp)
                            .background(Color(0xFF108888))
                            .padding(bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = getBitmap(resource = R.drawable.jetpack),
                            contentDescription = "",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Box(
                        Modifier
                            .height(40.dp)
                            .background(Color.Black)
                    )
                }
                Text(
                    text = "(4.6)", fontSize = 11.sp, modifier = Modifier
                        .width(40.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}


























/**
 * Compose UI的Itme
 * @param mainActions
 * @param uiData Image的ID
 */
@Composable
private fun TopListItem(
    mainActions: MainActions,
    imgId: Int,
) {
    val random = (0..360).random().toFloat()
    Column(
        Modifier
            .width(75.dp)
            .height(90.dp)
            .padding(start = 13.dp, top = 5.dp, bottom = 5.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = {
                mainActions.messageDetailsPage()
            }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = getBitmap(imgId),
            contentDescription = "1",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .rotate(random)

        )
        Text(
            text = stringResource(R.string.home_name),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

@Composable
private fun DrawBackCircleAndShaper(
    width: Dp = 126.dp,
    height: Dp = 48.dp,
    cicleHeight: Float = 60f,
    cycleRadio: Float = 50f,
    angularRadian: Float = 70f,
    colorShadow: Int = android.graphics.Color.argb(255, 221, 224, 231)
) {
    Canvas(modifier = Modifier
        .width(width)
        .height(height), onDraw = {
        drawIntoCanvas { canvas ->
            canvas.translate(0f, 10f)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true
            paint.strokeWidth = 10f
            paint.color = Color(255, 255, 255, 255).toArgb()
            paint.setShadowLayer(
                25f,
                5f,
                10f,
                colorShadow
            )
            val path = androidx.compose.ui.graphics.Path()
            path.moveTo(size.width, 0f)
            path.lineTo(angularRadian, 0f)
            path.quadraticBezierTo(0f, 0f, 0f, angularRadian)
            path.lineTo(0f, size.height - cicleHeight - angularRadian)
            path.quadraticBezierTo(
                0f,
                size.height - cicleHeight,
                angularRadian,
                size.height - cicleHeight
            )
            path.lineTo(angularRadian, size.height - cicleHeight)

            //下边弧度半径
            path.lineTo(size.width / 2 - cycleRadio, size.height - cicleHeight)
            path.quadraticBezierTo(
                size.width / 2,
                size.height,
                size.width / 2 + cycleRadio,
                size.height - cicleHeight
            )
            //弧度结束

            path.lineTo(size.width - angularRadian, size.height - cicleHeight)
            path.quadraticBezierTo(
                size.width,
                size.height - cicleHeight,
                size.width,
                size.height - cicleHeight - angularRadian
            )
            path.lineTo(size.width, angularRadian)
            path.quadraticBezierTo(size.width, 0f, size.width - angularRadian, 0f)
            path.close()
            //canvas.clipPath(path)
            //设置阴影,由于没有设置阴影所以需要转换为原生
            canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
        }
    })
}

@Stable
class ClipRowShaper(
    private var circleHeight: Float = 60f,
    private var cicleRaduis: Float = 50f,
    private var angularRadian: Float = 70f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = androidx.compose.ui.graphics.Path()
        path.moveTo(size.width, 0f)
        path.lineTo(angularRadian, 0f)
        path.quadraticBezierTo(0f, 0f, 0f, angularRadian)
        path.lineTo(0f, size.height - circleHeight - angularRadian)
        path.quadraticBezierTo(
            0f,
            size.height - circleHeight,
            angularRadian,
            size.height - circleHeight
        )
        path.lineTo(angularRadian, size.height - circleHeight)

        //下边弧度半径
        path.lineTo(size.width / 2 - cicleRaduis, size.height - circleHeight)
        path.quadraticBezierTo(
            size.width / 2,
            size.height,
            size.width / 2 + cicleRaduis,
            size.height - circleHeight
        )
        //弧度结束

        path.lineTo(size.width - angularRadian, size.height - circleHeight)
        path.quadraticBezierTo(
            size.width,
            size.height - circleHeight,
            size.width,
            size.height - circleHeight - angularRadian
        )
        path.lineTo(size.width, angularRadian)
        path.quadraticBezierTo(size.width, 0f, size.width - angularRadian, 0f)
        path.close()
        return Outline.Generic(path)
    }

}


fun Modifier.animalModifier(animalChange: Boolean, sizeAnimal: Float): Modifier = composed {
    //动画状态
    if (animalChange) {
        return@composed Modifier
            .width(sizeAnimal.dp)
            .height(sizeAnimal.dp)
    } else {//静止状态
        return@composed Modifier
    }

}
