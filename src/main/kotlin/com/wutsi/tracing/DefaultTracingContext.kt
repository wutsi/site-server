package com.wutsi.tracing

class DefaultTracingContext(
    private val requestId: String
) : TracingContext {
    override fun requestId() = requestId
    override fun clientId() = "NONE"
    override fun deviceId() = "NONE"
}
