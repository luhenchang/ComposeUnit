package com.example.composeunit.models.chatgtp

interface ChatGTPModel
sealed class ChatGTPFailModel(val errCode:Int) : ChatGTPModel {
    object NETTER : ChatGTPFailModel(101)
    object LOADER : ChatGTPFailModel(102)
}

data class ModelData(
    val choices: List<Choice>? = null,
    val created: Int? = null,
    val id: String? = null,
    val model: String? = null,
    val `object`: String? = null,
    val usage: Usage? = null
) : ChatGTPModel

data class Choice(
    val finish_reason: Any? = null,
    val index: Int? = null,
    val message: Message? = null
)

data class Usage(
    val completion_tokens: Int? = null,
    val prompt_tokens: Int? = null,
    val total_tokens: Int? = null
)

data class Message(
    val content: String? = null,
    val role: String? = null
)