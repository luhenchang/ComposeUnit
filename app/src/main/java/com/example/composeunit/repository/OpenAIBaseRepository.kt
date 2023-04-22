package com.example.composeunit.repository
import android.content.Context
import com.example.composeunit.repository.dao.table.ChatContent
import com.example.composeunit.repository.dao.table.ComposeData
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface OpenAIBaseRepository {
    fun queryChatList(context: Context): Flow<List<ChatContent>>
    suspend fun insertMessageToChatTable(context: Context, chatContent: ChatContent)
}