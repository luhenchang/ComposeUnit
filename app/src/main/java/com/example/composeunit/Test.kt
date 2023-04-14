package com.example.composeunit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

/**
 * Created by wangfei44 on 2023/4/13.
 */
object Test {
    val content = "可以使用System.out.println()函数打印字符串，例如：\n" +
            "\n" +
            "```java\n" +
            "public class PrintString {\n" +
            "    public static void main(String[] args) {\n" +
            "        String str = \"Hello, world!\";\n" +
            "        System.out.println(str);\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "输出：\n" +
            "\n" +
            "```\n" +
            "Hello, world!\n" +
            "```\n" +
            "我是中国人你以为你是谁"

    data class AnnotatedStringStyle(
        var fontStyle: SpanStyle,
        val start: Int,
        var end: Int
    )

    val input = """
    可以使用System.out.println()函数打印字符串，例如：
    ```java
    public class PrintString {
        public static void main(String[] args) {
            String str = "Hello, world!";
            System.out.println(str);
        }
    }
    ```
    输出：
    ```
    Hello, world!
    ```
"""

    @JvmStatic
    fun main(args: Array<String>) {
        val annotatedString = AnnotatedString.Builder()

        val input = """
    可以使用System.out.println()函数打印字符串，例如：
    ```java
    public class PrintString {
        public static void main(String[] args) {
            String str = "Hello, world!";
            System.out.println(str);
        }
    }
    ```
    输出：
    ```
    Hello, world!
    ```

    这是一段中文文本，不属于代码块。

    ```kotlin
    data class Person(val name: String, val age: Int)
    val person = Person("Alice", 20)
    println("Name: {person.name}, Age: {person.age}")
    ```

    中文文本结束。
""".split("```kotlin", "```java","```")
        input.forEach{
            val regex = "\\{[^{}]*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
            if (regex.containsMatchIn(it)) {
                annotatedString.apply {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(it)
                    }
                }
                println("code block=$it")
            }else{
                annotatedString.apply {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(it)
                    }
                }
                println("string block=$it")
            }
        }

    }
}