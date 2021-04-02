package com.wutsi.site.event

enum class EventType(
    val urn: String
) {
    INVALID("urn:event:wutsi:site:invalid"),
    CREATED("urn:event:wutsi:site:created"),
    UPDATED("urn:event:wutsi:site:updated")
}
