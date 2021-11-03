package com.example.composeunit.project.view_model.home
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.model.BaseViewModel
import com.example.composeunit.project.bean.Information
import com.example.composeunit.project.model.local.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: BaseViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(0)
    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position:LiveData<Int> = _position
    //选中索引数据刷新
    var bottomType=true

    //首页数据列表
    private val _informationList = MutableLiveData<List<Information>>()

    var  information:LiveData<List<Information>> = _informationList

    fun positionChanged(selectedIndex: Int){
        _position.value=selectedIndex
    }

    fun getInformation(current: Context) {
        viewModelScope.launch(Dispatchers.Main) {
           val information = getDbDate(current)
           _informationList.value = information
            Log.e("getInformation", "getInformation:${information[0].title}")
        }
    }

    private suspend fun getDbDate(current: Context) = withContext(Dispatchers.IO){
        HomeRepository.getListInformationFromLocalData(current)
    }
}