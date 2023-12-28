package com.example.composeunit.retrofit

import com.example.composeunit.data.models.chatgtp.ClientSendBody
import com.example.composeunit.data.models.chatgtp.ImageBody
import com.example.composeunit.data.models.chatgtp.ImageData
import com.example.composeunit.data.models.chatgtp.ModelData
import retrofit2.Call
import retrofit2.http.*


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

    @POST("images/generations")
    fun generateImage(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Body body: ImageBody
    ): Call<ImageData>
}