package com.wutsi.tracing

import java.util.UUID
import javax.servlet.http.HttpServletRequest
import kotlin.concurrent.getOrSet

class RequestTracingContext(
    private val request: HttpServletRequest
) : TracingContext {
    private val defaultRequestId: ThreadLocal<String> = ThreadLocal()

    override fun requestId() = request.getHeader(TracingContext.HEADER_REQUEST_ID) ?: defaultRequestId.getOrSet { UUID.randomUUID().toString() }
    override fun clientId() = request.getHeader(TracingContext.HEADER_CLIENT_ID) ?: "NONE"
    override fun deviceId() = request.getHeader(TracingContext.HEADER_DEVICE_ID) ?: "NONE"
}
