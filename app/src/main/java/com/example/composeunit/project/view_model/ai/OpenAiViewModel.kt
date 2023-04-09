package com.example.composeunit.project.view_model.ai

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeunit.models.chatgtp.*
import com.example.composeunit.retrofit.ChatGTPRepository
import com.example.composeunit.retrofit.HttpConst
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

    private var _startAnimal = MutableStateFlow(false)
    var startAnimal: StateFlow<Boolean> = _startAnimal

    fun setLoadValue(value: Boolean) {
        _loading.value = value
    }

    fun setStarAnimalValue(value: Boolean) {
        _startAnimal.value = value
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("handler", "exception = ${exception.message}")
        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
        val errorModel = ModelData(
            choices = arrayListOf(Choice(message = Message(content = exception.message))),
            isAI = true,
            errorNet = true
        )
        newList.add(errorModel)
        viewModelScope.launch {
            _pageList.emit(newList)
            updateLoadingState(errorModel)
        }
    }
    private var job: Job? = null
    private var genericjob: Job? = null

    fun cancelJob() {
        job?.apply {
            if (isActive) {
                cancel()
            }
        }
        genericjob?.apply {
            if (isActive) {
                cancel()
            }
        }
    }

    fun getChatGTPMessage(info: String) {
        job = viewModelScope.launch(handler) {
            flow {
                val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                if (newList.isNotEmpty()) {
                    when (val lastData = newList[newList.size - 1]) {
                        is ModelData -> {
                            if (!lastData.isAI) {//解决
                                newList.remove(lastData)
                            }
                        }
                        is ImageData -> {
                            if (!lastData.isAI) {//解决
                                newList.remove(lastData)
                            }
                        }
                    }
                }
                newList.add(
                    ModelData(
                        choices = arrayListOf(Choice(message = Message(content = info))),
                        isAI = false
                    )
                )
                _pageList.emit(newList)
                if (info.contains("生成图片")) {
                    emit(generateImage(info))
                } else {
                    Log.e("progress ==", "01")
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { result ->
                Log.e("progress ==", "03")
                when (result) {
                    is ChatGTPResult.Success -> {
                        _startAnimal.emit(true)
                        Log.e("progress ==", "04")
                        //新数据来了增加到集合
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(result.data)
                        //去刷新UI
                        Log.e("progress ==", "05")
                        _pageList.emit(newList)
                        updateLoadingState(result.data)
                    }
                    is ChatGTPResult.Fail -> {
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        val mode = ModelData(
                            choices = arrayListOf(Choice(message = Message(content = result.message))),
                            isAI = true,
                            errorNet = true
                        )
                        newList.add(mode)
                        _pageList.emit(newList)
                        updateLoadingState(mode)
                    }
                }
            }
        }
    }

    fun regenerateChatGTPMMessage(info: String) {
        genericjob = viewModelScope.launch(handler) {
            flow {
                if (info.contains("z生成图片")) {
                    emit(generateImage(info))
                } else {
                    Log.e("zprogress ==", "01")
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { info ->
                when (info) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        newList.add(info.data.apply { })
                        //去刷新UI
                        _pageList.emit(newList)
                        updateLoadingState(info.data)
                    }
                    is ChatGTPResult.Fail -> {
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        val mode = ModelData(
                            choices = arrayListOf(Choice(message = Message(content = info.message))),
                            isAI = true,
                            errorNet = true
                        )
                        newList.add(mode)
                        _pageList.emit(newList)
                    }
                }
            }
        }
    }

    private fun updateLoadingState(info: ChatGTPModel) {
        when (info) {
            is ModelData -> {
                Log.e("progress ==", "07")
                Log.e("pageList ModeData=", info.isAI.toString())
                if (info.isAI) {
                    setLoadValue(false)
                }
            }
            is ImageData -> {
                Log.e("progress ==", "08")
                Log.e("pageList ImageData=", info.toString())
                Log.e("pageList ImageData=", info.isAI.toString())
                if (info.isAI) {
                    setLoadValue(false)
                }
            }
        }
    }

    private suspend fun generateImage(
        prompt: String = "A cute baby sea otter",
        n: Int = 1,
        size: String = "256x256"
    ) = ChatGTPRepository.generateImage(
        HttpConst.CHAT_GTP_CONTENT_TYPE,
        HttpConst.CHAT_AUTHORIZATION,
        ImageBody(prompt, n, size)
    )

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