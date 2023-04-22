package com.example.composeunit.models.chatgtp

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import com.example.composeunit.repository.dao.table.ChatContent

/**
 * Created by wangfei44 on 2023/4/21.
 */
data class ChatGTPUIState(
    val loading: Boolean = false,
    val startAnimal: Boolean = false,
    val regenerateInfo: String = "",
    val dataList: ArrayList<ChatContent> = arrayListOf()
)