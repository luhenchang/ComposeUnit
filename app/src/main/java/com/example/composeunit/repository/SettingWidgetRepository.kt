package com.example.composeunit.repository

import com.example.composeunit.models.WidgetModel
import com.example.composeunit.repository.datastore.SettingWidgetDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by wangfei44 on 2022/11/20.
 */
class SettingWidgetRepository : WidgetRepository {
    override fun queryWidgets(): Flow<List<WidgetModel>> = flow {
        emit(SettingWidgetDataStore.allItemWidget)
    }
}