package com.example.composeunit.repository
import android.content.Context
import com.example.composeunit.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface DataBaseRepository {
    fun queryHomeLists(context: Context): Flow<List<User>>
}