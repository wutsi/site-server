package com.wutsi.site.dto

import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class Site(
    public val id: Long = 0,
    public val name: String = "",
    public val domainName: String = "",
    public val displayName: String = "",
    public val websiteUrl: String = "",
    public val language: String = "",
    public val currency: String = "",
    public val attributes: List<Attribute> = emptyList()
)
