package com.example.composeunit.data.repository
import android.content.Context
import com.example.composeunit.data.repository.dao.table.ComposeData
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface DataBaseRepository {
    fun queryHomeLists(context: Context): Flow<List<ComposeData>>
    suspend fun insertComposeData(context: Context, composeData: ComposeData)
}