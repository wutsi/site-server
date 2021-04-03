package com.wutsi.site.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.site.dao.AttributeRepository
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.SetAttributeRequest
import com.wutsi.site.event.EventType
import com.wutsi.site.event.UpdatedEventPayload
import com.wutsi.stream.EventStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SetAttributeController.sql"])
internal class SetAttributeControllerTest {
    @LocalServerPort
    private val port = 0

    private lateinit var url: String

    private val rest: RestTemplate = RestTemplate()

    @Autowired
    lateinit var siteDao: SiteRepository

    @Autowired
    lateinit var attrDao: AttributeRepository

    @MockBean
    lateinit var cacheManager: CacheManager

    @MockBean
    lateinit var cache: Cache

    @MockBean
    lateinit var eventStream: EventStream

    @BeforeEach
    fun setUp() {
        url = "http://127.0.0.1:$port/v1/site/{id}/attributes/{urn}"
        doReturn(cache).whenever(cacheManager).getCache("default")
    }

    @Test
    fun `update attribute`() {
        val request = SetAttributeRequest(
            value = "Long value of the attribute"
        )
        val urn = "urn:attribute:wutsi:attr1"
        val response = rest.postForEntity(url, request, Any::class.java, "1", urn)
        kotlin.test.assertEquals(OK, response.statusCode)

        val site = siteDao.findById(1L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn).get()
        assertEquals(request.value, attr.value)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(1, urn))

        verify(cache).evict(1L)
    }

    @Test
    fun `delete attribute when value if empty`() {
        val request = SetAttributeRequest(
            value = ""
        )
        val urn = "urn:attribute:wutsi:to-empty"
        rest.postForEntity(url, request, Any::class.java, "2", urn)

        val site = siteDao.findById(2L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn)
        assertFalse(attr.isPresent)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(2, urn))

        verify(cache).evict(2L)
    }

    @Test
    fun `delete attribute when value if null`() {
        val request = SetAttributeRequest(
            value = null
        )
        val urn = "urn:attribute:wutsi:to-null"
        rest.postForEntity(url, request, Any::class.java, "2", urn)

        val site = siteDao.findById(2L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn)
        assertFalse(attr.isPresent)
    }

    @Test
    fun `attr attribute`() {
        val request = SetAttributeRequest(
            value = "Yo"
        )
        val urn = "urn:attribute:wutsi:add-me"
        rest.postForEntity(url, request, Any::class.java, "1", urn)

        val site = siteDao.findById(1L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn).get()
        assertEquals(request.value, attr.value)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(1, urn))

        verify(cache).evict(1L)
    }
}
