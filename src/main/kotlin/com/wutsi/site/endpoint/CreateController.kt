package com.wutsi.site.endpoint

import com.wutsi.site.`delegate`.CreateDelegate
import com.wutsi.site.dto.CreateSiteRequest
import com.wutsi.site.dto.CreateSiteResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class CreateController(
    private val `delegate`: CreateDelegate
) {
    @PostMapping("/v1/sites")
    public fun invoke(@Valid @RequestBody request: CreateSiteRequest): CreateSiteResponse =
        delegate.invoke(request)
}
