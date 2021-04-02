package com.wutsi.site

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.`annotation`.EnableCaching
import org.springframework.transaction.`annotation`.EnableTransactionManagement
import kotlin.String

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class Application

public fun main(vararg args: String) {
    org.springframework.boot.runApplication<Application>(*args)
}
