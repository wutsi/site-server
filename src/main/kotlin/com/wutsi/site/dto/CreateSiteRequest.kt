package com.wutsi.site.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class CreateSiteRequest(
    @get:NotBlank
    public val name: String = "",
    @get:NotBlank
    public val domainName: String = "",
    public val displayName: String = "",
    @get:NotBlank
    public val language: String = "",
    @get:NotBlank
    public val currency: String = "",
    @get:NotBlank
    public val internationalCurrency: String = ""
)
