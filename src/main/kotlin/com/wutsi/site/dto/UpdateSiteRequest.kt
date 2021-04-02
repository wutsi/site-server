package com.wutsi.site.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class UpdateSiteRequest(
    @get:NotBlank
    public val name: String = "",
    @get:NotBlank
    public val domainName: String = "",
    public val displayName: String = ""
)
