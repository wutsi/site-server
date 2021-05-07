package com.wutsi.site.endpoint

import com.nhaarman.mockitokotlin2.verify
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.UpdateSiteRequest
import com.wutsi.site.dto.UpdateSiteResponse
import com.wutsi.site.event.SiteEventPayload
import com.wutsi.site.event.SiteEventType
import com.wutsi.stream.EventStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateController.sql"])
internal class UpdateControllerTest : ControllerTestBase() {
    @LocalServerPort
    private val port = 0

    @Autowired
    lateinit var dao: SiteRepository

    @MockBean
    lateinit var eventStream: EventStream

    @Test
    fun `update a site save into the DB`() {
        login("site.admin")

        val request = UpdateSiteRequest(
            name = "wutsI",
            displayName = "Wutsi",
            domainName = "wutsi.cUm",
            language = "fr",
            currency = "XFA"
        )
        val url = "http://127.0.0.1:$port/v1/sites/1"
        val response = post(url, request, UpdateSiteResponse::class.java)
        assertEquals(OK, response.statusCode)

        val site = dao.findById(1L).get()
        assertEquals(request.name.toLowerCase(), site.name)
        assertEquals(request.displayName, site.displayName)
        assertEquals(request.domainName.toLowerCase(), site.domainName)
        assertEquals(request.language, site.language)
        assertEquals(request.currency, site.currency)
    }

    @Test
    fun `update a site send UPDATE event`() {
        login("site.admin")

        val request = UpdateSiteRequest(
            name = "wutsI",
            displayName = "Wutsi",
            domainName = "wutsi.cUm",
            language = "fr",
            currency = "XFA"
        )
        val url = "http://127.0.0.1:$port/v1/sites/1"
        val response = post(url, request, UpdateSiteResponse::class.java)

        verify(eventStream).publish(SiteEventType.SITE_UPDATED.urn, SiteEventPayload(response.body.siteId))
    }

    @Test
    fun `anonymous cannot update a site`() {
        val request = UpdateSiteRequest(
            name = "wutsI",
            displayName = "Wutsi",
            domainName = "wutsi.cUm",
            language = "fr",
            currency = "XFA"
        )
        val url = "http://127.0.0.1:$port/v1/sites/1"
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, UpdateSiteResponse::class.java)
        }
        Assertions.assertEquals(FORBIDDEN, ex.statusCode)
    }

    @Test
    fun `user with invalid cannot update a site`() {
        login("site")

        val request = UpdateSiteRequest(
            name = "wutsI",
            displayName = "Wutsi",
            domainName = "wutsi.cUm",
            language = "fr",
            currency = "XFA"
        )
        val url = "http://127.0.0.1:$port/v1/sites/1"
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, UpdateSiteResponse::class.java)
        }
        Assertions.assertEquals(FORBIDDEN, ex.statusCode)
    }
}
