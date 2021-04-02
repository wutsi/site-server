package com.wutsi.site.endpoint

import com.wutsi.site.`delegate`.UpdateDelegate
import com.wutsi.site.dto.UpdateSiteRequest
import com.wutsi.site.dto.UpdateSiteResponse
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class UpdateController(
    private val `delegate`: UpdateDelegate
) {
    @PostMapping("/v1/site/{id}")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: UpdateSiteRequest
    ): UpdateSiteResponse = delegate.invoke(id, request)
}
