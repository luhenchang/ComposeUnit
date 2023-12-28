package com.example.composeunit.data.repository

import com.example.composeunit.data.models.WidgetModel
import com.example.composeunit.data.repository.datastore.SettingWidgetDataStore
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