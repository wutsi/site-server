package com.wutsi.site.event

data class UpdatedEventPayload(
    val siteId: Long,
    val attributeUrn: String? = null
)
