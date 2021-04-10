package com.wutsi.tracing

import org.slf4j.MDC
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class TracingFilter(
    private val tracingContextProvider: TracingContextProvider
) : Filter {
    override fun doFilter(
        req: ServletRequest,
        resp: ServletResponse,
        chain: FilterChain
    ) {
        val tc = tracingContextProvider.get()
        MDC.put("client_id", tc.clientId())
        MDC.put("device_id", tc.deviceId())
        MDC.put("request_id", tc.requestId())

        chain.doFilter(req, resp)
    }
}
