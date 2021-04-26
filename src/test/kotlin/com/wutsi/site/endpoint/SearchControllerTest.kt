package com.wutsi.site.endpoint

import com.wutsi.site.dto.SearchSiteResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchController.sql"])
public class SearchControllerTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest: RestTemplate = RestTemplate()

    @Test
    fun search() {
        val response = rest.getForEntity("http://127.0.0.1:$port/v1/sites", SearchSiteResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val sites = response.body.sites
        assertEquals(2, sites.size)

        assertEquals(1, sites[0].id)
        assertEquals("foo", sites[0].name)
        assertEquals("Foo Site", sites[0].displayName)

        assertEquals(2, sites[1].id)
        assertEquals("bar", sites[1].name)
        assertEquals("Bar Site", sites[1].displayName)
    }
}
