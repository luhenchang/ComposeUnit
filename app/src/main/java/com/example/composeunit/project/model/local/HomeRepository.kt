package com.example.composeunit.project.model.local

import android.content.Context
import com.example.composeunit.AppDatabase
import com.example.composeunit.User
import com.example.composeunit.repository.DataBaseRepository
import kotlinx.coroutines.flow.Flow
/**
 * Created by wangfei44 on 2021/9/16.
 */
class HomeRepository : DataBaseRepository {
    override fun queryHomeLists(context: Context): Flow<List<User>> {
        return AppDatabase.getDatabase(context).chinookDao().users
    }
}