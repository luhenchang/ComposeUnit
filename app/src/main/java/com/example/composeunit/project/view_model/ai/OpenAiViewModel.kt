package com.example.composeunit.project.view_model.ai

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeunit.models.chatgtp.*
import com.example.composeunit.project.model.local.OpenAIRepository
import com.example.composeunit.repository.dao.table.ChatContent
import com.example.composeunit.retrofit.ChatGTPRepository
import com.example.composeunit.retrofit.HttpConst
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by wangfei44 on 2023/4/7.
 */
class OpenAiViewModel(
    private val openAIRepository: OpenAIRepository = OpenAIRepository()
) : ViewModel() {
    private var _uiState = MutableStateFlow(ChatGTPUIState())
    var uiState: StateFlow<ChatGTPUIState> = _uiState.asStateFlow()

    fun setLoadValue(value: Boolean) {
        _uiState.update { it.copy(loading = value) }
    }

    fun getListSize(): Int = _uiState.value.dataList.size

    val getLastChatContent: ChatContent? get() = _uiState.value.dataList.lastOrNull()
    val startAnimal: Boolean get() = _uiState.value.startAnimal
    fun updateStarAnimalValue(value: Boolean) {
        _uiState.update {
            it.copy(startAnimal = value)
        }
    }

    private fun handler(context: Context) = CoroutineExceptionHandler { _, exception ->
        Log.e("handler", "exception = ${exception.message}")
        val errorModel = ModelData(
            choices = arrayListOf(Choice(message = Message(content = exception.message))),
            isAI = true,
            errorNet = true
        )
        viewModelScope.launch {
            getCustomMessage(exception.message, 0, 1, true)?.let { chatContent ->
                openAIRepository.insertMessageToChatTable(
                    context,
                    chatContent
                )
            }
            updateLoadingState(errorModel)
        }
    }

    private var job: Job? = null
    private var genericJob: Job? = null

    fun cancelJob() {
        job?.apply {
            if (isActive) {
                cancel()
            }
        }
        genericJob?.apply {
            if (isActive) {
                cancel()
            }
        }
    }

    fun updateUIState(context: Context) {
        viewModelScope.launch {
            openAIRepository.queryChatList(context).catch {

            }.collect { data ->
                _uiState.update {
                    it.copy(dataList = data as ArrayList<ChatContent>)
                }
            }
        }
    }

    fun getChatGTPMessage(context: Context, info: String) {
        job = viewModelScope.launch(handler(context)) {
            flow {
                //插入数据库
                getCustomMessage(info, 0, 0, false)?.let {
                    openAIRepository.insertMessageToChatTable(context, it)
                }
                if (info.contains("生成图片")) {
                    emit(generateImage(info))
                } else {
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        result.data?.let {
                            getChatResult(it).let { chatIt ->
                                getCustomMessage(
                                    chatIt.first,
                                    chatIt.second,
                                    1,
                                    false
                                )?.let { chatContent ->
                                    openAIRepository.insertMessageToChatTable(
                                        context,
                                        chatContent
                                    )
                                }
                            }
                            updateLoadingState(it)
                        }
                        _uiState.update {
                            it.copy(startAnimal = true)
                        }
                    }
                    is ChatGTPResult.Fail -> {
                        val mode = ModelData(
                            choices = arrayListOf(Choice(message = Message(content = result.message))),
                            isAI = true,
                            errorNet = true
                        )
                        getCustomMessage(result.message, 0, 1, true)?.let { chatContent ->
                            openAIRepository.insertMessageToChatTable(
                                context,
                                chatContent
                            )
                        }
                        updateLoadingState(mode)
                    }
                }
            }
        }
    }

    private fun getChatResult(it: ChatGTPModel): Pair<String?, Int> {
        return when (it) {
            is ModelData -> {
                Pair(it.choices?.get(0)?.message?.content, 0)
            }
            is ImageData -> {
                Pair(it.data?.get(0)?.url, 1)
            }
            else -> {
                Pair(null, -1)
            }
        }
    }

    private fun getCustomMessage(
        info: String?,
        content_type: Int,
        isAI: Int,
        errorNet: Boolean
    ): ChatContent? {
        val time = System.currentTimeMillis()
        if (info == null) {
            return null
        }
        return ChatContent(
            content_id = "content_$time",
            program_id = "program_$time",
            content = info,
            content_type = content_type,
            content_is_ai = isAI,
            content_fabulous = 0,
            errorNet = errorNet
        )
    }

    fun regenerateChatGTPMMessage(context: Context, info: String) {
        genericJob = viewModelScope.launch(handler(context)) {
            flow {
                if (info.contains("生成图片")) {
                    emit(generateImage(info))
                } else {
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { info ->
                when (info) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        info.data?.let {
                            getChatResult(it).let { chatIt ->
                                getCustomMessage(
                                    chatIt.first,
                                    chatIt.second,
                                    1,
                                    false
                                )?.let { chatContent ->
                                    openAIRepository.insertMessageToChatTable(
                                        context,
                                        chatContent
                                    )
                                }
                            }
                            updateLoadingState(it)
                        }

                    }
                    is ChatGTPResult.Fail -> {
                        val mode = ModelData(
                            choices = arrayListOf(Choice(message = Message(content = info.message))),
                            isAI = true,
                            errorNet = true
                        )
                        getCustomMessage(info.message, 0, 1, true)?.let { chatContent ->
                            openAIRepository.insertMessageToChatTable(
                                context,
                                chatContent
                            )
                        }
                        updateLoadingState(mode)
                    }
                }
            }
        }
    }

    private fun updateLoadingState(info: ChatGTPModel) {
        when (info) {
            is ModelData -> {
                if (info.isAI) {
                    setLoadValue(false)
                }
            }
            is ImageData -> {
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
        _uiState.update {
            it.copy(regenerateInfo = text)
        }
    }

    private suspend fun getMessage(info: String): ChatGTPResult<ChatGTPModel> {
        return ChatGTPRepository.getMessage(
            HttpConst.CHAT_GTP_CONTENT_TYPE, HttpConst.CHAT_AUTHORIZATION,
            ClientSendBody(
                listOf(ClientMessage(info, HttpConst.CHAT_GTP_ROLE)),
                HttpConst.CHAT_GTP_MODEL
            )
        )
    }
}