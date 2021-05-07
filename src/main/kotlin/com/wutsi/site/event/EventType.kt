package com.wutsi.site.event

enum class EventType(
    val urn: String
) {
    CREATED("urn:event:wutsi:site:created"),
    UPDATED("urn:event:wutsi:site:updated")
}
