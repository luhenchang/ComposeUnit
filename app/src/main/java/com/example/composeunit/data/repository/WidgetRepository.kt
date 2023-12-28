package com.example.composeunit.data.repository

import com.example.composeunit.data.models.WidgetModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface WidgetRepository {
    fun queryWidgets(): Flow<List<WidgetModel>>
}