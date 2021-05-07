package com.wutsi.site.`delegate`

import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.UpdateSiteRequest
import com.wutsi.site.dto.UpdateSiteResponse
import com.wutsi.site.event.SiteEventPayload
import com.wutsi.stream.EventStream
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
public class UpdateDelegate(
    private val dao: SiteRepository,
    private val eventStream: EventStream
) {
    @CacheEvict(value = ["default"], key = "#id")
    public fun invoke(id: Long, request: UpdateSiteRequest): UpdateSiteResponse {
        val site = dao.findById(id).get()
        site.displayName = request.displayName
        site.domainName = request.domainName.toLowerCase()
        site.name = request.name.toLowerCase()
        site.language = request.language.toLowerCase()
        site.currency = request.currency.toUpperCase()
        dao.save(site)

        eventStream.publish(com.wutsi.site.event.SiteEventType.SITE_UPDATED.urn, SiteEventPayload(id))

        return UpdateSiteResponse(
            siteId = id
        )
    }
}
