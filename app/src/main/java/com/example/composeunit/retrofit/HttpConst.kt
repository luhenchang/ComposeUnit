package com.example.composeunit.retrofit

/**
 * Created by wangfei44 on 2023/3/20.
 */
class HttpConst {
    companion object {
        private const val CHAT_GTP_KEY = "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"
        const val CHAT_AUTHORIZATION = "Bearer $CHAT_GTP_KEY"
        const val CHAT_GTP_CONTENT_TYPE = "application/json"
        const val CHAT_GTP_ROLE = "user"
        const val CHAT_GTP_MODEL = "gpt-3.5-turbo"
        const val CHAT_GTP_IMG_DEFAULT_SIZE = "256x256"
        val regex = Regex("生成图片|图片生成|来个图")
        val regexStart = Regex("生成|来个|画|绘")
        val regexEnd = Regex("图片|照片|画|山水图")
        const val CHAT_GTP_ERO =
            "An error occurred. Either the engine you requested does not exist or there was another issue processing your request. If this issue persists please contact us through our help center at help.openai.com."
    }
}