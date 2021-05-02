package com.wutsi.site.endpoint

import com.nhaarman.mockitokotlin2.verify
import com.wutsi.site.dao.AttributeRepository
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.SetAttributeRequest
import com.wutsi.site.event.EventType
import com.wutsi.site.event.UpdatedEventPayload
import com.wutsi.stream.EventStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SetAttributeController.sql"])
internal class SetAttributeControllerTest : ControllerTestBase() {
    @LocalServerPort
    private val port = 0

    @Autowired
    lateinit var siteDao: SiteRepository

    @Autowired
    lateinit var attrDao: AttributeRepository

    @MockBean
    lateinit var eventStream: EventStream

    @Test
    fun `update attribute`() {
        login("site.admin")

        val request = SetAttributeRequest(
            value = "Long value of the attribute"
        )
        val urn = "urn:attribute:wutsi:attr1"
        val url = "http://127.0.0.1:$port/v1/sites/1/attributes/$urn"
        val response = post(url, request, Any::class.java)
        kotlin.test.assertEquals(OK, response.statusCode)

        val site = siteDao.findById(1L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn).get()
        assertEquals(request.value, attr.value)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(1, urn))
    }

    @Test
    fun `delete attribute when value if empty`() {
        login("site.admin")

        val request = SetAttributeRequest(
            value = ""
        )
        val urn = "urn:attribute:wutsi:to-empty"
        val url = "http://127.0.0.1:$port/v1/sites/2/attributes/$urn"
        post(url, request, Any::class.java)

        val site = siteDao.findById(2L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn)
        assertFalse(attr.isPresent)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(2, urn))
    }

    @Test
    fun `delete attribute when value if null`() {
        login("site.admin")

        val request = SetAttributeRequest(
            value = null
        )
        val urn = "urn:attribute:wutsi:to-null"
        val url = "http://127.0.0.1:$port/v1/sites/2/attributes/$urn"
        post(url, request, Any::class.java)

        val site = siteDao.findById(2L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn)
        assertFalse(attr.isPresent)
    }

    @Test
    fun `add attribute`() {
        login("site.admin")

        val request = SetAttributeRequest(
            value = "Yo"
        )
        val urn = "urn:attribute:wutsi:add-me"
        val url = "http://127.0.0.1:$port/v1/sites/1/attributes/$urn"
        post(url, request, Any::class.java)

        val site = siteDao.findById(1L).get()
        val attr = attrDao.findBySiteAndUrn(site, urn).get()
        assertEquals(request.value, attr.value)

        verify(eventStream).publish(EventType.UPDATED.urn, UpdatedEventPayload(1, urn))
    }

    @Test
    fun `anonymous cannot set attr attribute`() {
        val request = SetAttributeRequest(
            value = "Yo"
        )
        val urn = "urn:attribute:wutsi:add-me"
        val url = "http://127.0.0.1:$port/v1/sites/1/attributes/$urn"
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, Any::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }

    @Test
    fun `user with invalid scope cannot set attr attribute`() {
        login("site")

        val request = SetAttributeRequest(
            value = "Yo"
        )
        val urn = "urn:attribute:wutsi:add-me"
        val url = "http://127.0.0.1:$port/v1/sites/1/attributes/$urn"
        val ex = assertThrows<HttpStatusCodeException> {
            post(url, request, Any::class.java)
        }
        assertEquals(HttpStatus.FORBIDDEN, ex.statusCode)
    }
}
