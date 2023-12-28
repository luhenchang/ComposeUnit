package com.example.composeunit.ui.compose.other.ai

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeunit.data.models.chatgtp.ChatContentIsAI
import com.example.composeunit.data.models.chatgtp.ChatContentType
import com.example.composeunit.data.models.chatgtp.ChatGTPModel
import com.example.composeunit.data.models.chatgtp.ChatGTPResult
import com.example.composeunit.data.models.chatgtp.ChatGTPUIState
import com.example.composeunit.data.models.chatgtp.ClientMessage
import com.example.composeunit.data.models.chatgtp.ClientSendBody
import com.example.composeunit.data.models.chatgtp.ImageBody
import com.example.composeunit.data.models.chatgtp.ImageData
import com.example.composeunit.data.models.chatgtp.ModelData
import com.example.composeunit.data.repository.OpenAIRepository
import com.example.composeunit.data.repository.dao.table.ChatContent
import com.example.composeunit.retrofit.ChatGTPRepository
import com.example.composeunit.retrofit.HttpConst
import com.example.composeunit.retrofit.HttpConst.Companion.CHAT_GTP_IMG_DEFAULT_SIZE
import com.example.composeunit.retrofit.HttpConst.Companion.regex
import com.example.composeunit.retrofit.HttpConst.Companion.regexEnd
import com.example.composeunit.retrofit.HttpConst.Companion.regexStart
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
        viewModelScope.launch {
            insertChatToTable(
                exception.message,
                ChatContentType.TEXT_TYPE,
                ChatContentIsAI.IS_AI,
                true,
                context
            )
            updateLoadingState()
        }
    }

    private var job: Job? = null

    fun cancelJob() {
        job?.apply {
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

    fun getChatGTPMessage(context: Context, info: String, reGenerated: Boolean = false) {
        job = viewModelScope.launch(handler(context)) {
            flow {
                if (!reGenerated) {
                    insertChatToTable(
                        info,
                        ChatContentType.TEXT_TYPE,
                        ChatContentIsAI.NOT_AI,
                        context = context
                    )
                }
                if (info.contains(regex) || (info.contains(regexStart) && info.contains(regexEnd))) {
                    emit(generateImage(info))
                } else {
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        result.data?.let {
                            val chatResult = getChatResult(it)
                            insertChatToTable(
                                chatResult.first,
                                chatResult.second,
                                ChatContentIsAI.IS_AI,
                                context = context
                            )
                            updateLoadingState()
                        }
                        _uiState.update {
                            it.copy(startAnimal = true)
                        }
                    }
                    is ChatGTPResult.Fail -> {
                        insertChatToTable(
                            result.message,
                            ChatContentType.TEXT_TYPE,
                            ChatContentIsAI.IS_AI,
                            true,
                            context
                        )
                        updateLoadingState()
                    }
                }
            }
        }
    }

    private suspend fun insertChatToTable(
        info: String?,
        content_type: Int,
        isAI: Int,
        errorNet: Boolean = false,
        context: Context
    ) {
        getCustomMessage(info, content_type, isAI, errorNet)?.let {
            openAIRepository.insertMessageToChatTable(context, it)
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

    private fun updateLoadingState() {
        setLoadValue(false)
    }

    private suspend fun generateImage(
        prompt: String,
        n: Int = 1,
        size: String = CHAT_GTP_IMG_DEFAULT_SIZE
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