package com.wutsi.site.config

import com.wutsi.tracing.RequestTracingContext
import com.wutsi.tracing.TracingContextProvider
import feign.RequestInterceptor
import org.springframework.beans.factory.`annotation`.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.`annotation`.Bean
import org.springframework.context.`annotation`.Configuration
import javax.servlet.Filter
import javax.servlet.http.HttpServletRequest
import kotlin.collections.List

@Configuration
public class TracingConfiguration(
    @Autowired
    private val request: HttpServletRequest,
    @Autowired
    private val context: ApplicationContext
) {
    @Bean
    public fun tracingFilter(): Filter = TracingFilter(tracingContextProvider())

    @Bean
    public fun requestTracingContext(): RequestTracingContext = RequestTracingContext(request)

    @Bean
    public fun tracingContextProvider(): TracingContextProvider = TracingContextProvider(context)

    @Bean
    public fun requestInterceptors(): List<RequestInterceptor> = listOf(
        TracingRequestInterceptor("site-server", tracingContextProvider())
    )
}
