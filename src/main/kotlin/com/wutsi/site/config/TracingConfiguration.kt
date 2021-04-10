package com.wutsi.site.config

import com.wutsi.tracing.RequestTracingContext
import com.wutsi.tracing.TracingContextProvider
import com.wutsi.tracing.TracingFilter
import com.wutsi.tracing.TracingRequestInterceptor
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter
import javax.servlet.http.HttpServletRequest

@Configuration
class TracingConfiguration(
    @Autowired private val request: HttpServletRequest,
    @Autowired private val context: ApplicationContext
) {
    @Bean
    fun tracingFilter(): Filter = TracingFilter(tracingContextProvider())

    @Bean
    fun requestTracingContext() = RequestTracingContext(request)

    @Bean
    fun tracingContextProvider() = TracingContextProvider(context)

    @Bean
    fun requestInterceptors(): List<RequestInterceptor> =
        listOf(
            TracingRequestInterceptor("site-server", tracingContextProvider())
        )
}
