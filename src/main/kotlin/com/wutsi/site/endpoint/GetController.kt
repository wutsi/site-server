package com.wutsi.site.endpoint

import com.wutsi.site.`delegate`.GetDelegate
import com.wutsi.site.dto.GetSiteResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class GetController(
    private val `delegate`: GetDelegate
) {
    @GetMapping("/v1/sites/{id}")
    @PreAuthorize(value = "hasAuthority('site')")
    public fun invoke(@PathVariable(name = "id") id: Long): GetSiteResponse = delegate.invoke(id)
}
