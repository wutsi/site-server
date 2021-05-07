package com.wutsi.site.event

enum class SiteEventType(
    val urn: String
) {
    SITE_CREATED("urn:event:wutsi:site:created"),
    SITE_UPDATED("urn:event:wutsi:site:updated")
}
