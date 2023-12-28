package com.example.composeunit.ui.compose.other

import android.graphics.Color
import android.text.SpannableString
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lib_common.utils.*
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import android.text.TextPaint


const val textContentString =
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

//static List<int> get darkColor => [
//0xFFFFFFFF, //基础
//0xFFDF935F, //数字
//0xFF9E9E9E, //注释
//0xFF80CBC4, //关键字
//0xFFB9CA4A, //字符串
//0xFFFFFFFF, //标点符号
//0xFF7AA6DA, //类名
//0xFF795548, //常量
//0xFF1D1F21, //背景
//];

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun TextPage(visible: Boolean) {
    val strSplit = textContentString.split("\n")
    AnimatedVisibility(visible = visible) {
        Box(
            Modifier
                .padding(top = 15.dp)
        ) {
            Column(
                Modifier
                    .background(
                        color = composeColor(225, 1, 1, 1),
                        shape = RoundedCornerShape(3)
                    )
                    .padding(26.dp)
            ) {
                for (element in strSplit) {
                    val result: SpannableString = RegexUtils(
                        codeSetConfing(
                            textColor = Color.WHITE,
                            keywordColor = composeColorLong(0xFF80CBC4).toArgb(),
                            valueColor = composeColorLong(0xFFB9CA4A).toArgb(),
                            attributeColor = Color.WHITE,
                            parameterColor = composeColorLong(0xFF795548).toArgb()
                        ),
                        element
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Start) {
                            AndroidView(
                                factory = { context ->
                                    TextView(context)
                                },
                                update = { view ->
                                    val tp: TextPaint = view.paint
                                    tp.isFakeBoldText = true
                                    tp.textSize=36f
                                    view.text = result
                                }
                            )
                        }
                    }
                }
            }
        }
    }


}

