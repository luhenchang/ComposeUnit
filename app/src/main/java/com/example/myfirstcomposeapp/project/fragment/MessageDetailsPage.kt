package com.example.myfirstcomposeapp.project.fragment
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.lib_common.utils.AndroidShare
import com.example.lib_common.utils.pxToDp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.confing.AppDivider
import com.example.myfirstcomposeapp.confing.MainActions
import com.example.myfirstcomposeapp.confing.MyTopAppBar
import com.example.myfirstcomposeapp.project.fragment.twofragments.TextPage
import com.example.myfirstcomposeapp.project.fragment.twofragments.textContentString
import com.example.myfirstcomposeapp.ui.theme.blue
import com.example.myfirstcomposeapp.utils.getBitmap
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.glide.GlideImage
import com.google.accompanist.glide.LocalRequestManager
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
@Composable
fun MessageDetailsPage(mainActions: MainActions) {
    val tapX = remember { mutableStateOf(0f) }
    val tapY = remember { mutableStateOf(0f) }
    Log.e("staticContext.density", "MessageDetailsPage: " + LocalContext.current)
    val expanded = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
//     if (openDialog.value) {
//         Dialog(onDismissRequest = { openDialog.value = false }) {
//             Box(){
//                Image(bitmap = getBitmap(resource = R.drawable.alertdlog), contentDescription = null)
//             }
//         }
//     }
//副标题
//    //region Popup
//    Box(Modifier.fillMaxWidth()) {
//        Row(modifier = Modifier.padding(top = 300.dp)) {
//            Text("hello", Modifier.width(100.dp))
//            Text(
//                modifier = Modifier
//                    .pointerInput(Unit) {
//                        coroutineScope {
//                            while (true) {
//                                val awaitPoint = awaitPointerEventScope {
//                                    awaitFirstDown().position
//                                }
//                                launch {
//                                    tapX.value = awaitPoint.x
//                                    tapY.value = awaitPoint.y
//                                    expanded.value = !expanded.value
//                                    openDialog.value = !openDialog.value
//
//                                }
//
//                            }
//                        }
//                    }, text = "点击按钮"
//            )
//        }
//    }
////    if (openDialog.value) {
////        Box(
////            Modifier.fillMaxWidth().fillMaxHeight()//.offset((tapX.value / 3.0f).dp, (tapY.value / 3.0f).dp)
////        ) {
////            val popupWidth = 200.dp
////            val popupHeight = 350.dp
////            val cornerSize = 16.dp
////            Popup(
////                alignment = Alignment.TopCenter,
////                offset = IntOffset(tapX.value.toInt(), tapY.value.toInt()),
////                onDismissRequest = { openDialog.value = false }) {
////                Box(
////                    Modifier
////                        .size(popupWidth, popupHeight)
////                        .background(Color.Red, RoundedCornerShape(cornerSize))
////                )
////            }
////        }
////    }
//
////endregion
    /**
     * 下拉菜单
     */
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        properties = PopupProperties(securePolicy = SecureFlagPolicy.SecureOn),
        offset = DpOffset(tapX.value.pxToDp(), tapY.value.pxToDp()),
    ) {
        DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
            Text("Refresh")
        }
        DropdownMenuItem(onClick = { /* Handle settings! */ }) {
            Text("Settings")
        }
        Divider()
        DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
            Text("Send Feedback")
        }
    }
    FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Center) {
        for (i in 0 until 100) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(Random.nextInt(40, 155).dp)
                    .height(30.dp)
                    .background(
                        Color(
                            Random.nextInt(255),
                            Random.nextInt(255),
                            Random.nextInt(255),
                            233
                        )
                    )
                    .padding(5.dp)
                    .clickable {
                        openDialog.value = !openDialog.value
                    }
            ) {
                Text(text = "Hello")
            }
        }
    }
}

@Composable
fun GlideApp() {
    val glide = Glide.with(LocalContext.current)
    CompositionLocalProvider(LocalRequestManager provides glide) {
        GlideImage(
            data = R.drawable.animal_gif,
            contentDescription = "My content description",
            contentScale = ContentScale.Fit
        )
    }
}


/**
 * 详情页面
 *
 */
@Composable
fun MessageDetailPage(mainActions: MainActions) {
    Scaffold(
        topBar = { MyTopAppBar(mainActions) }, backgroundColor = Color(238, 239, 247, 255)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            topConlayout()
            AppDivider()
            LinkCompose()
            AppDivider(10.dp)
            CodeCompose()

        }
    }
}


/**
 * 代码部分
 */
@Preview
@Composable
fun CodeCompose() {
    val visibleCodeState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    Column(Modifier.padding(15.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .shadow(5.dp, CircleShape)
                    .background(blue, CircleShape)
                    .clip(CircleShape)
                    .padding(end = 5.dp)
            )
            Box(Modifier.width(15.dp))
            Text(
                text = "文字的基本样式",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(200.dp)
            )
            Spacer(Modifier.weight(1f))
            Image(
                bitmap = getBitmap(resource = R.drawable.shared_icon),
                colorFilter = tint(blue),
                contentDescription = null,
                modifier = Modifier
                    .width(23.dp)
                    .height(20.dp)
                    .clickable {
                        AndroidShare.shareWeChatFriend(
                            "这是标题",
                            textContentString,
                            AndroidShare.TEXT,
                            null
                        )
                    },
                contentScale = ContentScale.FillBounds,

                )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                bitmap = getBitmap(resource = R.drawable.code),
                colorFilter = tint(blue),
                contentDescription = null,
                modifier = Modifier
                    .width(23.dp)
                    .height(20.dp)
                    .clickable(
                        role = Role.Image
                    ) {
                        visibleCodeState.value = !visibleCodeState.value
                    }
                    .rotate(rotationAngle(visibleCodeState.value)),
                contentScale = ContentScale.FillBounds,
            )
        }
        //代码样式部分
        TextPage(visibleCodeState.value)
    }
}

fun rotationAngle(value: Boolean): Float {
    return if (value) 90f else 0f
}

/**
 * 相关组件
 */
@Composable
fun LinkCompose() {
    Column(
        Modifier.padding(15.dp)
    ) {
        Row {
            Image(
                bitmap = getBitmap(resource = R.mipmap.link),
                contentDescription = "",
                Modifier
                    .width(29.dp)
                    .height(28.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = "相关组件",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(200.dp)
                    .padding(bottom = 15.dp, start = 5.dp)
            )
        }
        FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Start) {
            FlowRowItem(
                "RichText"
            )
            Spacer(modifier = Modifier.width(5.dp))
            FlowRowItem(
                "DefaultTextStyle"
            )
        }
    }
}

/**
 * 相关组件的里的圆形按钮
 */
@Composable
private fun FlowRowItem(text: String) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .background(
                blue,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))


    ) {
        Box(Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Text(text = text, color = Color.White)
        }
    }
}

/**
 * 详情顶部描述
 */
@Composable
fun topConlayout() {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (left_cm, right_cm) = createRefs()
        Column(Modifier.constrainAs(left_cm) {
            top.linkTo(parent.top, margin = 20.dp)
            start.linkTo(parent.start, margin = 15.dp)
        }) {
            Text(
                text = "文字组件",
                color = Color(106, 180, 241, 255),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(200.dp)
                    .padding(bottom = 15.dp)
            )
            Text(
                modifier = Modifier
                    .width(200.dp)
                    .background(color = Color(106, 180, 241, 11), shape = RoundedCornerShape(10))
                    .padding(6.dp),
                text = "用于显示文字的组件。拥有的属性非常多,足够满足你的需求，核心样式由style属性控制",
                fontSize = 16.sp,
                lineHeight = 1.2f.em

            )
        }
        Column(Modifier.constrainAs(right_cm) {
            end.linkTo(parent.end, margin = 10.dp)
            top.linkTo(parent.top, margin = 20.dp)
            start.linkTo(left_cm.end, margin = 5.dp)
        }) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(90.dp)
                    .background(color = Color(106, 180, 241, 22), shape = RoundedCornerShape(10))

            ) {

            }

            Canvas(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 10.dp)
            ) {
                drawIntoCanvas {
                    val path = Path()
                    val paint = Paint()
                    paint.color = Color(106, 180, 241, 255)
                    paint.style = PaintingStyle.Fill
                    val fivePoints = fivePoints(50f, 0f, 50)
                    for (index in 0 until 4) {
                        it.translate(50f, 0f)
                        for (indexIn in fivePoints.indices) {
                            path.lineTo(fivePoints[indexIn].x, fivePoints[indexIn].y)
                        }
                        it.drawPath(path, paint)
                    }

                }
            }

        }
    }
}

/**
 * @param xA 起始点位置A的x轴绝对位置
 * @param yA 起始点位置A的y轴绝对位置
 * @param rFive 五角星边的边长
 */
fun fivePoints(xA: Float, yA: Float, rFive: Int): ArrayList<PointF> {
    val yB: Float
    val yD: Float
    val yE: Float
    val xD: Float = (xA - rFive * sin(Math.toRadians(18.0))).toFloat()
    val xC: Float = (xA + rFive * sin(Math.toRadians(18.0))).toFloat()
    val yC: Float = (yA + cos(Math.toRadians(18.0)) * rFive).toFloat()
    yD = yC
    yE = (yA + sqrt(
        (xC - xD).toDouble().pow(2.0) - (rFive / 2).toDouble().pow(2.0)
    )).toFloat()
    yB = yE
    val xB: Float = xA + rFive / 2
    val xE: Float = xA - rFive / 2
    val pointA = PointF(xA, yA)
    val pointD = PointF(xD, yD)
    val pointB = PointF(xB, yB)
    val pointE = PointF(xE, yE)
    val pointC = PointF(xC, yC)
    return arrayListOf(pointA, pointD, pointB, pointE, pointC, pointA)
}




