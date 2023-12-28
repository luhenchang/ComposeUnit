package com.example.composeunit.data.models

import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface WidgetRepository {
    fun queryAllWidget(): Flow<List<WidgetModel>>
}