package com.example.composeunit.data.models.chatgtp

/**
 * Created by wangfei44 on 2023/4/6.
 */
data class ImageData(
    val created: Int = 0,
    val `data`: List<Data>? = null,
    val userData: String? = "",
    var isAI: Boolean = true,
    var errorNet: Boolean = false,
) : ChatGTPModel

data class Data(
    val url: String? = null
)