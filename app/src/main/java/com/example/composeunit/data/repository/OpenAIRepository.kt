package com.example.composeunit.data.repository

import android.content.Context
import com.example.composeunit.data.repository.OpenAIBaseRepository
import com.example.composeunit.data.repository.dao.AppDatabase
import com.example.composeunit.data.repository.dao.table.ChatContent
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2023/4/20.
 */
class OpenAIRepository : OpenAIBaseRepository {
    override fun queryChatList(context: Context): Flow<List<ChatContent>> {
        return AppDatabase.getDatabase(context).chinookDao().chatContent
    }

    override suspend fun insertMessageToChatTable(context: Context, chatContent: ChatContent) {
        AppDatabase.getDatabase(context).chinookDao().insertMessageToChatTable(chatContent)
    }
}