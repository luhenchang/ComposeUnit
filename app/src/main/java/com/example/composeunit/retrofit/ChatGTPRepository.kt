package com.example.composeunit.retrofit

import android.util.Log
import com.example.composeunit.models.chatgtp.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * Created by wangfei44 on 2023/3/20.
 */
object ChatGTPRepository {
    suspend fun getMessage(
        type: String, authorization: String, body: ClientSendBody
    ): ChatGTPResult<ModelData> = withContext(Dispatchers.IO) {
        val response = RetrofitManger.service.getMessage(type, authorization, body).awaitResponse()
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body()!!)
        } else {
            ChatGTPResult.Fail(response.code(), response.message().toString())
        }
    }

    suspend fun generateImage(
        type: String, authorization: String, imageBody: ImageBody
    ): ChatGTPResult<ChatGTPModel> = withContext(Dispatchers.IO) {
        val response =
            RetrofitManger.service.generateImage(type, authorization, imageBody).awaitResponse()
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body() as ImageData)
        } else {
            ChatGTPResult.Fail(response.code(), response.message().toString())
        }
    }
}