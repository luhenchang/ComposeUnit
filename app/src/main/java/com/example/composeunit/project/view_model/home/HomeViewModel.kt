package com.example.composeunit.project.view_model.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.model.BaseViewModel
import com.example.composeunit.User
import com.example.composeunit.models.chatgtp.*
import com.example.composeunit.project.model.local.HomeRepository
import com.example.composeunit.repository.DataBaseRepository
import com.example.composeunit.retrofit.ChatGTPRepository
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_AUTHORIZATION
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_GTP_CONTENT_TYPE
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_GTP_MODEL
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_GTP_ROLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
    private val _itemsUIState = MutableStateFlow<List<User>>(emptyList())

    val itemUIState: StateFlow<List<User>> = _itemsUIState

    fun positionChanged(selectedIndex: Int) {
        _position.value = selectedIndex
    }

    var responseData = MutableStateFlow<ChatGTPModel>(ModelData())
    private fun getChatGTPMessage(info: String) {
        viewModelScope.launch {
            ChatGTPRepository.getMessage(
                CHAT_GTP_CONTENT_TYPE, CHAT_AUTHORIZATION,
                ClientSendBody(listOf(ClientMessage(info, CHAT_GTP_ROLE)), CHAT_GTP_MODEL)
            ).let { result ->
                when (result) {
                    is ChatGTPResult.Success -> {
                        result.data.choices?.let {
                            Log.e("result==", it[0].message?.content.toString())
                        }
                        responseData.emit(result.data)
                    }
                    is ChatGTPResult.Fail -> {
                        Log.e("result==", result.errCode.toString())
                        responseData.emit(if (result.errCode == 101) ChatGTPFailModel.LOADER else ChatGTPFailModel.NETTER)
                    }
                }
            }

        }
    }

    fun getInformation(current: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            var index = 0
            repository.queryHomeLists(current).catch { ex ->
                Log.e("queryHomeLists::error=", ex.message.toString())
            }.collect { data ->
                Log.e("getInformation", "getInformation:${data[0].name}")
                Log.e("getInformation", "index = $index")
                index++
                _itemsUIState.emit(data)
            }
        }
    }

}