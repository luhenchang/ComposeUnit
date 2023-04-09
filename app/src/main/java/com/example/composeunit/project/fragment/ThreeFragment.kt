package com.example.composeunit.project.fragment

import android.content.Context
import android.widget.TextView
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.composeunit.R
import com.example.composeunit.confing.MainActions
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.neo.highlight.core.Highlight
import com.neo.highlight.core.Scheme
import com.neo.highlight.util.scheme.ColorScheme
import com.neo.highlight.util.scheme.Scope
import java.util.regex.Pattern

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
        CodeHighlighter(code,actions)
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

private fun getKotlinScheme(context: Context): List<Scheme> {
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