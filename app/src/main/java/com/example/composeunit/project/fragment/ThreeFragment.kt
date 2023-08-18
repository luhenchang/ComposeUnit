package com.example.composeunit.project.fragment

import android.content.Context
import android.text.SpannableString
import android.text.TextPaint
import android.widget.TextView
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.composeunit.R
import com.example.composeunit.ui.compose.confing.MainActions
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.neo.highlight.core.Highlight
import com.neo.highlight.core.Scheme
import com.neo.highlight.util.scheme.ColorScheme
import com.neo.highlight.util.scheme.Scope
import java.util.regex.Pattern
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.example.lib_common.utils.RegexUtils
import com.example.lib_common.utils.codeSetConfing
import com.google.accompanist.flowlayout.FlowMainAxisAlignment

/**
 * Created by wangfei44 on 2022/11/19.
 */
val code = "@Composable\n" +
        "fun ThreeFragment(){\n" +
        "    Column(\n" +
        "    Modifier\n" +
        "    .verticalScroll(ScrollState(0))\n" +
        "    .padding(bottom = 20.dp)\n" +
        "    ) {\n" +
        "        CodeHighlighter()\n" +
        "    }\n" +
        "}"

@Composable
fun ThreeFragment(actions: MainActions) {
    Column(
        Modifier
            .verticalScroll(ScrollState(0))
            .padding(bottom = 20.dp)
    ) {
        CodeHighlighter(code, actions)
    }
}

@Composable
fun CodeHighlighter(code: String, actions: MainActions) {
    AndroidView(factory = {
        val tv = TextView(it)
        val highlight = Highlight()
        highlight.schemes = getKotlinScheme(it)
        tv.text = code
        highlight.setSpan(tv)
        tv
    }, Modifier.clickable(true, onClick = {
        print("能不能退货？")
        actions.openAIPage()
    }))
}


@Preview
@Composable
fun SpannableText(content:String?) {
    val widgetList: ArrayList<Unit> = ArrayList()
    content?.split("```kotlin", "```java", "```")?.forEach { text ->
        val regex = "\\{[^{}]*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
        if (regex.containsMatchIn(text)) {
            val topCode =Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(52, 54, 65, 255))
                    .height(30.dp).clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "code",color = Color(216, 216, 226, 255))
                Text(text = "Copy code",color = Color(216, 216, 226, 255))
            }
            widgetList.add(topCode)
            val strSplit = text.split("\n")
            for (element in strSplit) {
                val result: SpannableString = RegexUtils(
                    codeSetConfing(
                        textColor = android.graphics.Color.WHITE,
                        keywordColor = Color(203, 120, 50, 255).toArgb(),
                        valueColor = Color(106, 133, 89, 255).toArgb(),
                        attributeColor = Color(255, 119, 64, 142).toArgb(),
                        parameterColor = Color(250, 250, 250, 255).toArgb()
                    ),
                    element
                )
                val codeBox = Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    com.google.accompanist.flowlayout.FlowRow(mainAxisAlignment = FlowMainAxisAlignment.Start) {
                        AndroidView(
                            modifier = Modifier
                                .background(Color.Black)
                                .fillMaxWidth(),
                            factory = { context ->
                                TextView(context)
                            },
                            update = { view ->
                                val tp: TextPaint = view.paint
                                tp.isFakeBoldText = true
                                tp.textSize = 36f
                                view.text = result
                            }
                        )
                    }
                }
                widgetList.add(codeBox)
            }
        } else {
           val selectionContainer = SelectionContainer {
                Text(
                    text = text,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                )
            }
            widgetList.add(selectionContainer)
        }
    }
}

fun getKotlinScheme(context: Context): List<Scheme> {
    val schemes: MutableList<Scheme> = ArrayList()
    //keywords

    schemes.add(
        ColorScheme(
            Pattern.compile("\\b(fun)|(val)|(var)|(sp)|(dp)|(private)\\b"),
            ContextCompat.getColor(context, R.color.keyword)
        )
    )

    // 数字
    schemes.add(
        Scope(
            Pattern.compile("\\w+")
        ).addScopeScheme(
            ColorScheme(
                Pattern.compile("^(\\d+\\.\\d+)|^(\\d+)"),
                ContextCompat.getColor(context, R.color.num)
            )
        )
    )
    // class
    schemes.add(
        Scope(
            Pattern.compile("\\w+")
        ).addScopeScheme(
            ColorScheme(
                Pattern.compile("^[A-Z](.*?)\\w+"),
                ContextCompat.getColor(context, R.color.clazz)
            )
        )
    )

    //function declaration
    schemes.add(
        Scope(
            Pattern.compile("\\b(fun)\\b\\s*\\b\\w+\\b\\([^()]*\\)")
        ).addScopeScheme(
            ColorScheme(
                Pattern.compile("\\w*(?=\\()"),
                ContextCompat.getColor(context, R.color.function)
            )
        )
    )


    //annotations
    schemes.add(
        ColorScheme(
            Pattern.compile("@.+"),
            ContextCompat.getColor(context, R.color.annotation)
        )
    )

    //strings
    schemes.add(
        ColorScheme(
            Pattern.compile("\"[^\"]*\""),
            ContextCompat.getColor(context, R.color.string)
        )
    )
    return schemes
}