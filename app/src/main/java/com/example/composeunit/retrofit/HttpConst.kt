package com.example.composeunit.retrofit

/**
 * Created by wangfei44 on 2023/3/20.
 */
class HttpConst {
    companion object {
        private const val CHAT_GTP_KEY = "自己的API key"
        const val CHAT_AUTHORIZATION = "Bearer $CHAT_GTP_KEY"
        const val CHAT_GTP_CONTENT_TYPE = "application/json"
        const val CHAT_GTP_ROLE = "user"
        const val CHAT_GTP_MODEL = "gpt-3.5-turbo"

        const val CHAT_GTP_ERO = "An error occurred. Either the engine you requested does not exist or there was another issue processing your request. If this issue persists please contact us through our help center at help.openai.com."
    }
}