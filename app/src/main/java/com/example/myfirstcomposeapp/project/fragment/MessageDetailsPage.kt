package com.example.myfirstcomposeapp.project.fragment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.bumptech.glide.Glide
import com.example.lib_common.utils.pxToDp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.confing.MainActions
import com.example.myfirstcomposeapp.utils.getBitmap
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.glide.LocalRequestManager
import kotlin.random.Random
@Composable
fun MessageDetailsPage(mainActions: MainActions) {
    val tapX = remember { mutableStateOf(0f) }
    val tapY = remember { mutableStateOf(0f) }
    Log.e("staticContext.density", "MessageDetailsPage: "+ LocalContext.current)
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
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        properties = PopupProperties(securePolicy = SecureFlagPolicy.SecureOn),
        offset = DpOffset(tapX.value.pxToDp(),tapY.value.pxToDp()),
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
                Text(text ="Hello")
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


