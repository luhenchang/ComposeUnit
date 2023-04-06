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
    }
}