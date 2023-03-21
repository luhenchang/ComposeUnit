package com.example.composeunit.models.chatgtp;

/**
 * Created by wangfei44 on 2023/3/20.
 */
data class ClientSendBody(
    val messages: List<ClientMessage>? = null,
    val model: String? = null
)

data class ClientMessage(
    val content: String? = null,
    val role: String? = null
)