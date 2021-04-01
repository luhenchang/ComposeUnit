import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import com.example.myfirstcomposeapp.confing.MainActions
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessLow
import kotlinx.coroutines.coroutineScope
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myfirstcomposeapp.composeble_ui.login.*
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


@InternalComposeApi
@Composable
fun LoginPage(mainActions: MainActions) {
    val animatedOffset = remember { Animatable(0f) }
    val animatedScales = remember { Animatable(1f) }
    val animatedRound = remember { Animatable(30f) }
    val animatedCheckBox = remember { Animatable(0f) }
    val animatedBitmap = remember { Animatable(0f) }
    val animatedText = remember { Animatable(1f) }
    val animatedColor = remember { Animatable(Color(206, 199, 250, 121)) }
    val mutableState: MutableState<Boolean> = remember { mutableStateOf(true) }
    val inputUserName: MutableState<String> = remember { mutableStateOf("") }
    val inputPassworld: MutableState<String> = remember { mutableStateOf("") }
    Box {
        Box {
            LoginPageBackgroundBlurImage(animatedRound, animatedScales)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.pointerInput(Unit) {
                    coroutineScope {
                        while (true) {
                            val offset = awaitPointerEventScope {
                                awaitFirstDown().position

                            }
                            // Launch a new coroutine for animation so the touch detection thread is not
                            // blocked.
                            launch {
                                if (mutableState.value) {
                                    animatedScales.animateTo(
                                        1.3f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedText.animateTo(
                                        1f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedRound.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedBitmap.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedCheckBox.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedColor.animateTo(Color(206, 170, 209, 121))
                                } else {
                                    animatedScales.animateTo(
                                        1f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedText.animateTo(
                                        -2f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedRound.animateTo(
                                        30f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedBitmap.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedCheckBox.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedColor.animateTo(Color(206, 199, 250, 121))
                                }

                                mutableState.value = !mutableState.value
                                animatedOffset.animateTo(
                                    offset.x,
                                    animationSpec = spring(stiffness = StiffnessLow)
                                )

                            }

                        }
                    }
                }) {
                Box(contentAlignment = Alignment.Center) {
                    LoginPageTopBlurImage(animatedBitmap, animatedOffset, animatedScales)
                    LoginPageTopRotaAndScaleImage(animatedColor, animatedScales, animatedOffset)
                }
                TextStudy()
                LoginPageTopTextBox(animatedOffset, animatedScales)
                LoginPageInput(inputUserName, animatedColor, animatedRound, inputPassworld)
                LoginPageCheckBox(animatedCheckBox, animatedOffset, animatedRound)
                LoginPageBootomButton(animatedScales, animatedColor, mainActions)

            }
        }
    }
}

val textContentString =
            "@Preview()\n" +
            "@Composable\n" +
            "fun TextStudy1() {\n" +
            "    Text(\n" +
            "        text = \"hello world\",\n" +
            "        modifier = Modifier.fillMaxWidth(),\n" +
            "        style = TextStyle(\n" +
            "            color = Color.Red,\n" +
            "            shadow = Shadow(\n" +
            "                color = Color.Green,\n" +
            "                offset = Offset(2f, 2f),\n" +
            "                blurRadius = 1f\n" +
            "            )\n" +
            "        )\n" +
            "    )\n" +
            "}"

@Preview(name = "textStudy")
@Composable
fun TextStudy() {
    val strSplit = textContentString.split("\n")
    for (element in strSplit) {
        Log.e("TextStudy", "TextStudy= " + element)
        val arrayString =
            arrayOf("=", "text", "style", "blurRadius", "color", "shadow", "color", "offset")
        val result: SpannableString = matcherSearchTitle(
            android.graphics.Color.argb(255, 136, 166, 240),
            element,
            arrayString
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Start) {
                AndroidView(
                    factory = { context ->
                        TextView(context)
                    },
                    update = { view ->
                        view.text = result
                    }
                )
            }
        }
    }

}

@Composable()
fun matcherSearchTitle(color: Int, text: String?, keyword: Array<String>): SpannableString {
    val s = SpannableString(text)
    //头部的Compose和PreView修饰
    if (s.contains("@")){
        s.setSpan(ForegroundColorSpan(android.graphics.Color.argb(255,131,132,8)), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    for (i in keyword.indices) {
        val p = Pattern.compile(keyword[i])
        val m = p.matcher(s)
        while (m.find()) {
            val start = m.start()
            val end = m.end()
            s.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    //被""标记的字符串。
    matcherPattern(android.graphics.Color.argb(255, 52, 131, 0), s, "\"", "\"")
    //组件属性 Color.Blue的Color部分设置
    matcherPattern(android.graphics.Color.argb(255, 119, 64, 142), s, "\\.", "\\,")
    //参数颜色设置-例如 Offset(2f, 2f)前面中的 2f
    matcherPattern(android.graphics.Color.argb(255, 15, 0, 255), s, "\\(", "\\,")
    //参数颜色设置-例如 Offset(2f, 2f)中后面的 2f
    matcherPattern(android.graphics.Color.argb(255, 15, 0, 255), s, "\\(", "\\),")
    return s
}

@Composable
private fun matcherPattern(color: Int, s: SpannableString, starPn: String, endPn: String) {
    val rex = "(?<=${starPn}).*?(?=${endPn})"
    val pn = Pattern.compile(rex)
    val matcher = pn.matcher(s)
    while (matcher.find()) {
        val start = matcher.start()
        val end = matcher.end()
        Log.e("matcherResult", "matcherPattern: " + s.toString().substring(start, end))
        if (!s.toString().substring(start, end).contains("=")&&s.toString().substring(start, end).length>1) {
            s.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    }
}

@Preview()
@Composable
fun TextStudy1() {
    Text(
        text = "hello world",
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            color = Color.Red,
            shadow = Shadow(
                color = Color.Green,
                offset = Offset(2f, 2f),
                blurRadius = 1f,
            )
        )
    )
}