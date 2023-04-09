package com.example.composeunit.models.chatgtp

/**
 * Created by wangfei44 on 2023/4/6.
 */
data class ImageData(
    val created: Int,
    val `data`: List<Data>,
    val userData: String = "",
    val isAI: Boolean = true,
    val errorNet: Boolean = false
) : ChatGTPModel

data class Data(
    val url: String
)