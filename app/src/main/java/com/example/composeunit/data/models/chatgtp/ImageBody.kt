package com.example.composeunit.data.models.chatgtp

/**
 * Created by wangfei44 on 2023/4/6.
 */
data class ImageBody(
    var prompt: String,
    var n: Int,
    var size: String
)