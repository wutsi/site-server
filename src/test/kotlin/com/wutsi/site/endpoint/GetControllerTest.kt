package com.wutsi.site.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.site.dto.Attribute
import com.wutsi.site.dto.GetSiteResponse
import com.wutsi.site.dto.Site
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import kotlin.test.Ignore
import kotlin.test.fail

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetController.sql"])
internal class GetControllerTest {
    @LocalServerPort
    private val port = 0

    private lateinit var url: String

    private val rest: RestTemplate = RestTemplate()

    @MockBean
    lateinit var cacheManager: CacheManager

    @MockBean
    lateinit var cache: Cache

    @BeforeEach
    fun setUp() {
        url = "http://127.0.0.1:$port/v1/sites/{id}"
        doReturn(cache).whenever(cacheManager).getCache("default")
    }

    @Test
    fun `return site from DB`() {
        val response = rest.getForEntity(url, GetSiteResponse::class.java, "1")
        assertEquals(OK, response.statusCode)

        val site = response.body.site
        assertEquals(1L, site.id)
        assertEquals("foo", site.name)
        assertEquals("Foo Site", site.displayName)
        assertEquals("foo.com", site.domainName)
        assertEquals("https://foo.com", site.websiteUrl)

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
    fun `cache returned value from DB`() {
        val response = rest.getForEntity(url, GetSiteResponse::class.java, "1")
        assertEquals(OK, response.statusCode)

        verify(cache).put(1L, response.body)
    }

    @Test
    @Ignore
    fun `return site from Cache`() {
        val value = GetSiteResponse(
            Site(
                id = 555L,
                name = "Yo",
                displayName = "Man",
                domainName = "yo.man",
                attributes = listOf(
                    Attribute("urn:attribute:wutsi:attr1", "value1")
                )
            )
        )
        doReturn(value).whenever(cache).get(555L, GetSiteResponse::class.java)

        val response = rest.getForEntity(url, GetSiteResponse::class.java, "555")
        assertEquals(OK, response.statusCode)

        val site = response.body.site
        assertEquals(value.site, site)
    }

    @Test
    fun `return 404 with invalid ID`() {
        try {
            rest.getForEntity(url, GetSiteResponse::class.java, "999")
            fail()
        } catch (ex: HttpStatusCodeException) {
            assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)
        }
    }
}
