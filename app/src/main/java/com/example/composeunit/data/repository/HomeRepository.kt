package com.example.composeunit.data.repository

import android.content.Context
import com.example.composeunit.data.repository.dao.AppDatabase
import com.example.composeunit.data.repository.dao.table.ComposeData
import com.example.composeunit.data.repository.DataBaseRepository
import kotlinx.coroutines.flow.Flow
/**
 * Created by wangfei44 on 2021/9/16.
 */
class HomeRepository : DataBaseRepository {
    override fun queryHomeLists(context: Context): Flow<List<ComposeData>> {
        return AppDatabase.getDatabase(context).chinookDao().composeData
    }

    override suspend fun insertComposeData(context: Context, composeData: ComposeData) {
        AppDatabase.getDatabase(context).chinookDao().insertComposeData(composeData)
    }
}