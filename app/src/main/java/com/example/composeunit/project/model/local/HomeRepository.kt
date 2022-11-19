package com.example.composeunit.project.model.local

import android.content.Context
import com.example.composeunit.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by wangfei44 on 2021/9/16.
 */
object HomeRepository {
   suspend fun getListInformationFromLocalData(mContext: Context) = withContext(Dispatchers.IO) {
        AppDatabase.getDatabase(mContext).chinookDao().users.collect {

        }
    }
}