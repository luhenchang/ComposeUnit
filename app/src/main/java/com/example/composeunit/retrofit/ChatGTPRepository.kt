package com.example.composeunit.retrofit

import androidx.compose.ui.platform.LocalContext
import com.example.composeunit.models.chatgtp.ChatGTPResult
import com.example.composeunit.models.chatgtp.ClientSendBody
import com.example.composeunit.models.chatgtp.ModelData
import com.example.lib_common.utils.NetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.io.IOException

/**
 * Created by wangfei44 on 2023/3/20.
 */
object ChatGTPRepository {
    suspend fun getMessage(
        type: String,
        authorization: String,
        body: ClientSendBody
    ): ChatGTPResult<ModelData> = withContext(Dispatchers.IO) {
        try {
            val response =
                RetrofitManger.service.getMessage(type, authorization, body).awaitResponse()
            if (response.isSuccessful) {
                ChatGTPResult.Success(response.body()!!)
            } else {
                ChatGTPResult.Fail(101)
            }
        } catch (e: Exception) {
            ChatGTPResult.Fail(101)
        }
    }
}