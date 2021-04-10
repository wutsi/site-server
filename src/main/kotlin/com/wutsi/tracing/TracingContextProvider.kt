package com.wutsi.tracing

import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import java.util.UUID
import kotlin.concurrent.getOrSet

open class TracingContextProvider(
    private val context: ApplicationContext
) {
    private val tc: ThreadLocal<TracingContext> = ThreadLocal()

    fun get(): TracingContext {
        if (RequestContextHolder.getRequestAttributes() != null) {
            return context.getBean(RequestTracingContext::class.java)
        } else {
            return tc.getOrSet {
                DefaultTracingContext(requestId = UUID.randomUUID().toString())
            }
        }
    }
}
