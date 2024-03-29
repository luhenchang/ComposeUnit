package com.example.composeunit.data.models.chatgtp

import com.example.composeunit.data.repository.dao.table.ChatContent

/**
 * Created by wangfei44 on 2023/4/21.
 */
data class ChatGTPUIState(
    val loading: Boolean = false,
    val startAnimal: Boolean = false,
    val regenerateInfo: String = "",
    val dataList: ArrayList<ChatContent> = arrayListOf()
)

class ChatContentType {
    companion object {
        const val TEXT_TYPE = 0
        const val IMG_TYPE = 1
        const val FILE_TYPE = 2
    }

}

class ChatContentIsAI {
    companion object {
        const val IS_AI = 1
        const val NOT_AI = 0
    }

}