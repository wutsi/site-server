package com.wutsi.site.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.CreateSiteRequest
import com.wutsi.site.dto.CreateSiteResponse
import com.wutsi.site.event.SiteEventPayload
import com.wutsi.site.event.SiteEventType
import com.wutsi.stream.EventStream
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql"])
internal class CreateControllerTest : ControllerTestBase() {
    @LocalServerPort
    private val port = 0

    private lateinit var url: String

    @Autowired
    lateinit var dao: SiteRepository

    @MockBean
    lateinit var cacheManager: CacheManager

    @MockBean
    lateinit var cache: Cache

    @MockBean
    lateinit var eventStream: EventStream

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://127.0.0.1:$port/v1/sites"
        doReturn(cache).whenever(cacheManager).getCache("default")
    }

    @Test
    fun `create a site is stored in DB`() {
        login("site.admin")

        val request = CreateSiteRequest(
            name = "wutsI",
            displayName = "Wutsi",
            domainName = "wutsi.cOm",
            language = "fr",
            currency = "XFA"
        )
        val response = post(url, request, CreateSiteResponse::class.java)
        assertEquals(OK, response.statusCode)

        val site = dao.findById(response.body.siteId).get()
        assertEquals(request.name.toLowerCase(), site.name)
        assertEquals(request.displayName, site.displayName)
        assertEquals(request.domainName.toLowerCase(), site.domainName)
        assertEquals(request.language, site.language)
        assertEquals(request.currency, site.currency)
    }

    @Test
    fun `create a site fires CREATED event`() {
        login("site.admin")

        val request = CreateSiteRequest(
            name = "wutsi",
            displayName = "Wutsi",
            domainName = "wutsi.cOm",
            language = "fr",
            currency = "XFA"
        )
        val response = post(url, request, CreateSiteResponse::class.java)

        verify(eventStream).publish(SiteEventType.SITE_CREATED.urn, SiteEventPayload(response.body.siteId))
    }

    @Test
    fun `anonymous cannot create site`() {
        val request = CreateSiteRequest(
            name = "wutsi",
            displayName = "Wutsi",
            domainName = "wutsi.cOm",
            language = "fr",
            currency = "XFA"
        )
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, CreateSiteResponse::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }

    @Test
    fun `user with invalid scope cannot create site`() {
        login("site")
        val request = CreateSiteRequest(
            name = "wutsi",
            displayName = "Wutsi",
            domainName = "wutsi.cOm",
            language = "fr",
            currency = "XFA"
        )
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, CreateSiteResponse::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }
}
