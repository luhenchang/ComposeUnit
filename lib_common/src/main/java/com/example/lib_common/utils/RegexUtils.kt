package com.example.lib_common.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import java.util.regex.Pattern

/**
 *@param keywordColor 关键字颜色
 *@param valueColor 字符串颜色例 如文字"Hello Wold"
 *@param attributeColor 属性颜色 例如 Color.RED 中的Color
 *@param parameterColor 参数的颜色 例如 Offset(2f,2f) 中的 2f 和 2f
 */
data class CodeLightColor(
    var textColor: Int,
    var keywordColor: Int,
    var valueColor: Int,
    var attributeColor: Int,
    var parameterColor: Int
)

/**
 *@param keywordColor 关键字颜色
 *@param valueColor 字符串颜色例 如文字"Hello Wold"
 *@param attributeColor 属性颜色 例如 Color.RED 中的RED
 *@param parameterColor 参数的颜色 例如 Offset(2f,2f) 中的 2f 和 2f
 */
fun codeSetConfing(
    textColor: Int = Color.BLACK,
    keywordColor: Int = Color.argb(255, 139, 167, 238),
    valueColor: Int = Color.argb(255, 52, 131, 0),
    attributeColor: Int = Color.argb(255, 119, 64, 142),
    parameterColor: Int = Color.argb(255, 15, 0, 255)
): CodeLightColor = CodeLightColor(
    textColor,
    keywordColor,
    valueColor,
    attributeColor,
    parameterColor
)

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
/**
 * @代码正则表达式设置高亮功能
 * @param codeSetConfig 字的颜色配置参数
 * @param text 文字
 * return  SpannableString
 */
@Composable()
fun RegexUtils(codeSetConfig: CodeLightColor, text: String?): SpannableString {
    val s = SpannableString(text)
    //设置基本的颜色
    s.setSpan(
        ForegroundColorSpan(codeSetConfig.textColor),
        0,
        s.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    //头部的Compose和PreView修饰
    if (s.contains("@")) {
        s.setSpan(
            ForegroundColorSpan(Color.argb(255, 131, 132, 8)),
            0,
            s.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    for (i in keywords.indices) {
        val p = Pattern.compile(keywords[i])
        val m = p.matcher(s)
        while (m.find()) {
            val start = m.start()
            val end = m.end()
            s.setSpan(
                ForegroundColorSpan(codeSetConfig.keywordColor),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    //被""标记的字符串。
    matcherPattern(codeSetConfig.valueColor, s, "\"", "\"")
    matcherPattern(composeColorLong(0xFF7AA6DA).toArgb(), s, "\\=", "\\.")
    //组件属性 Color.Blue的Blue部分设置
    matcherPattern(codeSetConfig.attributeColor, s, "\\.", "\\,")
    //参数颜色设置-例如 Offset(2f, 2f)前面中的 2f
    matcherPattern(codeSetConfig.parameterColor, s, "\\(", "\\,")
    //参数颜色设置-例如 Offset(2f, 2f)中后面的 2f
    matcherPattern(codeSetConfig.parameterColor, s, "\\(", "\\),")
    return s
}

/**
 *
 */
@Composable
private fun matcherPattern(color: Int, s: SpannableString, starPn: String, endPn: String) {
    val rex = "(?<=${starPn}).*?(?=${endPn})"
    val pn = Pattern.compile(rex)
    val matcher = pn.matcher(s)
    while (matcher.find()) {
        val start = matcher.start()
        val end = matcher.end()
        Log.e("matcherResult", "matcherPattern: " + s.toString().substring(start, end))
        if (!s.toString().substring(start, end).contains("=") && s.toString()
                .substring(start, end).length > 1
        ) {
            s.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    }
}

val keywords = arrayOf("=", "text", "style", "blurRadius", "color", "shadow", "color", "offset")

