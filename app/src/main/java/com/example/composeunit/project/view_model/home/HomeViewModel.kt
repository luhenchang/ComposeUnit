package com.example.composeunit.project.view_model.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.model.BaseViewModel
import com.example.composeunit.repository.dao.table.ComposeData
import com.example.composeunit.project.model.local.HomeRepository
import com.example.composeunit.repository.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: DataBaseRepository = HomeRepository()
) : BaseViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(0)

    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position: LiveData<Int> = _position

    //首页数据列表
    private val _itemsUIState = MutableStateFlow<List<ComposeData>>(emptyList())

    val itemUIState: StateFlow<List<ComposeData>> = _itemsUIState

    fun positionChanged(selectedIndex: Int) {
        _position.value = selectedIndex
    }

    fun getInformation(current: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.queryHomeLists(current).catch { ex ->
                Log.e("queryHomeLists::error=", ex.message.toString())
            }.collect { data ->
                Log.e("getInformation", "getInformation:${data[0].item_title}")
                _itemsUIState.emit(data)
            }
        }
    }

    fun insertComposeData(current: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertComposeData(current,
                ComposeData(2,"Column","垂直容器布局","Compose 提供了一系列现成可用的布局来帮助您排列界面元素，并可让您轻松定义自己的更专业布局，使用 Column 可将多个项垂直地放置在屏幕上",5)
            )
        }
    }

}