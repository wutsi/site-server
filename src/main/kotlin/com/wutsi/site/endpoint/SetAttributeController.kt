package com.wutsi.site.endpoint

import com.wutsi.site.`delegate`.SetAttributeDelegate
import com.wutsi.site.dto.SetAttributeRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long
import kotlin.String

@RestController
public class SetAttributeController(
    private val `delegate`: SetAttributeDelegate
) {
    @PostMapping("/v1/sites/{id}/attributes/{urn}")
    @PreAuthorize(value = "hasAuthority('site.admin')")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @PathVariable(name = "urn") urn: String,
        @Valid @RequestBody request: SetAttributeRequest
    ) {
        delegate.invoke(id, urn, request)
    }
}
