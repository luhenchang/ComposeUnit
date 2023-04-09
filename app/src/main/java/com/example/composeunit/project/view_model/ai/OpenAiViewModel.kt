package com.example.composeunit.project.view_model.ai

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeunit.models.chatgtp.*
import com.example.composeunit.retrofit.ChatGTPRepository
import com.example.composeunit.retrofit.HttpConst
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_GTP_ERO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by wangfei44 on 2023/4/7.
 */
class OpenAiViewModel : ViewModel() {
    private var _pageList = MutableStateFlow<ArrayList<ChatGTPModel>>(arrayListOf())
    var pageList: StateFlow<ArrayList<ChatGTPModel>> = _pageList

    private var _generateInfo = MutableStateFlow("")
    var regenerateResponseInfo: StateFlow<String> = _generateInfo

    private var _loading = MutableStateFlow(false)
    var loading: StateFlow<Boolean> = _loading

    fun setLoadValue(value :Boolean){
        _loading.value = value
    }

    private var _responseData = MutableStateFlow<ChatGTPModel?>(null)
    private var _bitmapData = MutableStateFlow<ChatGTPModel?>(null)
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("handler", "exception = ${exception.message}")
        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
        newList.add(
            ModelData(
                choices = arrayListOf(Choice(message = Message(content = exception.message))),
                isAI = true,
                errorNet = true
            )
        )
        viewModelScope.launch {
            _pageList.emit(newList)
        }
    }
    private var job: Job? = null
    private var genericjob: Job? = null

    fun cancelJob() {
        Log.e("cancelJob", "()")
        job?.apply {
            Log.e("cancelJob", isActive.toString())
            if (isActive) {
                Log.e("cancelJob", isCancelled.toString())
                cancel()
            }
        }
        genericjob?.apply {
            Log.e("cancelJob1", isActive.toString())
            if (isActive) {
                Log.e("cancelJob1", isCancelled.toString())
                cancel()
            }
        }
    }

    fun getChatGTPMessage(info: String) {
        if (info.contains("生成图片")){
            generateImage()
        }
        job = viewModelScope.launch(handler) {
            flow {
                val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                newList.add(
                    ModelData(
                        choices = arrayListOf(Choice(message = Message(content = info))),
                        isAI = false
                    )
                )
                _pageList.emit(newList)
                emit(getMessage(info))
            }.flowOn(Dispatchers.IO).collect { info ->
                when (info) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(info.data)
                        //去刷新UI
                        _pageList.emit(newList)
                    }
                    is ChatGTPResult.Fail -> {
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(
                            ModelData(
                                choices = arrayListOf(Choice(message = Message(content = info.message))),
                                isAI = true,
                                errorNet = true
                            )
                        )
                        _pageList.emit(newList)
                    }
                }
            }
        }
    }

    fun regenerateChatGTPMMessage(info: String) {
        genericjob = viewModelScope.launch(handler) {
            flow {
                emit(getMessage(info))
            }.flowOn(Dispatchers.IO).collect { info ->
                when (info) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(info.data.apply { })
                        //去刷新UI
                        _pageList.emit(newList)
                    }
                    is ChatGTPResult.Fail -> {
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(
                            ModelData(
                                choices = arrayListOf(Choice(message = Message(content = info.message))),
                                isAI = true,
                                errorNet = true
                            )
                        )
                        _pageList.emit(newList)
                    }
                }
            }
        }
    }

    private fun generateImage(
        prompt: String = "A cute baby sea otter",
        n: Int = 1,
        size: String = "256x256"
    ) {
        viewModelScope.launch {
            ChatGTPRepository.generateImage(
                HttpConst.CHAT_GTP_CONTENT_TYPE,
                HttpConst.CHAT_AUTHORIZATION,
                ImageBody(prompt, n, size)
            ).apply {
                when (this) {
                    is ChatGTPResult.Success -> {
                        _bitmapData.emit(this.data)
                    }
                    is ChatGTPResult.Fail -> {
                        _bitmapData.emit(ChatGTPFailModel.NETTER)
                    }
                }

            }
        }
    }

    fun setRegenerateInfo(text: String) = viewModelScope.launch {
        _generateInfo.emit(text)
    }
}

private suspend fun getMessage(info: String): ChatGTPResult<ModelData> {
    return ChatGTPRepository.getMessage(
        HttpConst.CHAT_GTP_CONTENT_TYPE, HttpConst.CHAT_AUTHORIZATION,
        ClientSendBody(
            listOf(ClientMessage(info, HttpConst.CHAT_GTP_ROLE)),
            HttpConst.CHAT_GTP_MODEL
        )
    )
}