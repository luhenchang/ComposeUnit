package com.example.composeunit.project.view_model.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.model.BaseViewModel
import com.example.composeunit.User
import com.example.composeunit.project.model.local.HomeRepository
import com.example.composeunit.repository.DataBaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    val repository: DataBaseRepository = HomeRepository()
) : BaseViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(0)

    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position: LiveData<Int> = _position

    //选中索引数据刷新
    var bottomType = true

    //首页数据列表
    private val _itemsUIState = MutableStateFlow<List<User>>(emptyList())

    val itemUIState: StateFlow<List<User>> = _itemsUIState

    fun positionChanged(selectedIndex: Int) {
        _position.value = selectedIndex
    }

    fun getInformation(current: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.queryHomeLists(current).catch { ex ->
                Log.e("queryHomeLists::error=", ex.message.toString())
            }.collect { data ->
                Log.e("getInformation", "getInformation:${data[0].name}")
                _itemsUIState.value = data as List<User>
            }
        }
    }

}