package com.example.composeunit.models.chatgtp

/**
 * Created by wangfei44 on 2023/3/20.
 */
sealed class ChatGTPResult<out T : Any> {
    data class Success<out T : Any>(val data: T?) : ChatGTPResult<T>()
    data class Fail(val errCode: Int, val message: String = "") : ChatGTPResult<Nothing>()
}