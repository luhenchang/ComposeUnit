package com.example.composeunit.retrofit

import com.example.composeunit.models.chatgtp.ClientSendBody
import com.example.composeunit.models.chatgtp.ModelData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by wangfei44 on 2023/3/20.
 */
interface ApiService {
    @POST("chat/completions")
    fun getMessage(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Body body: ClientSendBody
    ): Call<ModelData>
}