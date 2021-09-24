package com.example.composeunit.project.view_model.home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composeunit.project.bean.Information
import com.example.composeunit.project.model.local.HomeRepository

class HomeViewModel: ViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(0)
    //动画状态
    val animalBoolean = mutableStateOf(true)
    var position:LiveData<Int> = _position
    //选中索引数据刷新
    var bottomType=true

    //首页数据列表
    var informationList = MutableLiveData<List<Information>>()
    fun positionChanged(selectedIndex: Int){
        _position.value=selectedIndex
    }
    @Composable
    fun getInformation(){
        HomeRepository.getListInformationFromLocalData(
            informationCallBack = object : HomeRepository.InformationLocalInterface{
                override fun onSuccess(information: List<Information>) {
                    informationList.value = information
                }

                override fun onFail(e: String) {

                }


            }
        )
    }
}