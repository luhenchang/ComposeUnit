package com.example.composeunit.retrofit

import android.util.Log
import com.example.composeunit.models.chatgtp.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

/**
 * Created by wangfei44 on 2023/3/20.
 */
object ChatGTPRepository {
    suspend fun getMessage(
        type: String, authorization: String, body: ClientSendBody
    ): ChatGTPResult<ModelData> = withContext(Dispatchers.IO) {
        val response = RetrofitManger.service.getMessage(type, authorization, body).awaitResponse()
        Log.e(
            "response msg=",
            "body=${response.body().toString()}" + ":message=${response.message()}"
        )
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body()!!)
        } else {
            ChatGTPResult.Fail(response.code(), response.message().messageResult())
        }
    }

    suspend fun generateImage(
        type: String, authorization: String, imageBody: ImageBody
    ): ChatGTPResult<ImageData> = withContext(Dispatchers.IO) {
        val response =
            RetrofitManger.service.generateImage(type, authorization, imageBody).awaitResponse()
        Log.e(
            "response img =",
            "body=${response.body().toString()}" + ":message=${response.message()}"
        )
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body()!!)
        } else {
            ChatGTPResult.Fail(response.code(), response.message().messageResult())
        }
    }
}

fun String.messageResult(): String = if (isNullOrEmpty()) "unknown error" else this