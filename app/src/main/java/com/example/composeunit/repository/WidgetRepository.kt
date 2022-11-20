package com.example.composeunit.repository

import com.example.composeunit.models.WidgetModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
interface WidgetRepository {
    fun queryWidgets(): Flow<List<WidgetModel>>
}