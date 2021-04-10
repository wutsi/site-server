package com.wutsi.tracing

interface TracingContext {
    companion object {
        const val HEADER_REQUEST_ID = "X-Request-ID"
        const val HEADER_CLIENT_ID = "X-Client-ID"
        const val HEADER_DEVICE_ID = "X-Device-ID"
    }

    fun requestId(): String
    fun clientId(): String
    fun deviceId(): String
}
