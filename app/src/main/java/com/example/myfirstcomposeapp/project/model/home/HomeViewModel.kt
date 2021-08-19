package com.example.myfirstcomposeapp.project.model.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(0)
    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position:LiveData<Int> = _position
    //选中索引数据刷新
    var bottomType=true
    fun positionChanged(selectedIndex: Int){
        _position.value=selectedIndex
    }
}