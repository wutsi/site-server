package com.wutsi.site.endpoint

import com.wutsi.site.dto.GetSiteResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetController.sql"])
internal class GetControllerTest : ControllerTestBase() {
    @LocalServerPort
    private val port = 0

    @Test
    fun `return site from DB`() {
        login("site-read")

        val url = "http://127.0.0.1:$port/v1/sites/1"
        val response = get(url, GetSiteResponse::class.java)
        assertEquals(OK, response.statusCode)

        val site = response.body.site
        assertEquals(1L, site.id)
        assertEquals("foo", site.name)
        assertEquals("Foo Site", site.displayName)
        assertEquals("foo.com", site.domainName)
        assertEquals("https://foo.com", site.websiteUrl)
        assertEquals("fr", site.language)
        assertEquals("XAF", site.currency)
        assertEquals("EUR", site.internationalCurrency)

        val attrs = site.attributes.sortedBy { it.urn }
        assertEquals(3, attrs.size)

        assertEquals("urn:attribute:wutsi:attr1", attrs[0].urn)
        assertEquals("value1", attrs[0].value)

        assertEquals("urn:attribute:wutsi:attr2", attrs[1].urn)
        assertEquals("value2", attrs[1].value)

        assertEquals("urn:attribute:wutsi:attr3", attrs[2].urn)
        assertEquals("value3", attrs[2].value)
    }

    @Test
    fun `return 404 with invalid siteId`() {
        login("site-read")

        val url = "http://127.0.0.1:$port/v1/sites/000"
        val ex = assertThrows<HttpStatusCodeException> {
            get(url, GetSiteResponse::class.java)
        }
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)
    }

    @Test
    fun `anonymous cannot get site`() {
        val url = "http://127.0.0.1:$port/v1/sites/000"
        val ex = assertThrows<HttpStatusCodeException> {
            get(url, GetSiteResponse::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }

    @Test
    fun `user with invalid scope cannot get site`() {
        login("xxx")

        val url = "http://127.0.0.1:$port/v1/sites/000"
        val ex = assertThrows<HttpStatusCodeException> {
            get(url, GetSiteResponse::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }
}
