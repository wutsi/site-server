package com.wutsi.site.endpoint

import com.wutsi.site.`delegate`.SearchDelegate
import com.wutsi.site.dto.SearchSiteResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RequestParam
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Int

@RestController
public class SearchController(
    private val `delegate`: SearchDelegate
) {
    @GetMapping("/v1/sites")
    @PreAuthorize(value = "hasAuthority('site-read')")
    public fun invoke(
        @RequestParam(name = "limit", required = false, defaultValue = "20") limit: Int = 20,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int = 0
    ):
        SearchSiteResponse = delegate.invoke(limit, offset)
}
