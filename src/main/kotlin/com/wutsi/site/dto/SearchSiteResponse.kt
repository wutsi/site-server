package com.wutsi.site.dto

import kotlin.collections.List

public data class SearchSiteResponse(
    public val sites: List<SiteSummary> = emptyList()
)
