package com.wutsi.site.`delegate`

import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.UpdateSiteRequest
import com.wutsi.site.dto.UpdateSiteResponse
import com.wutsi.site.event.UpdatedEventPayload
import com.wutsi.stream.EventStream
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
public class UpdateDelegate(
    private val dao: SiteRepository,
    private val eventStream: EventStream
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(UpdateDelegate::class.java)
    }

    @CacheEvict(value = ["default"], key = "#id")
    public fun invoke(id: Long, request: UpdateSiteRequest): UpdateSiteResponse {
        val site = dao.findById(id).get()
        site.displayName = request.displayName
        site.domainName = request.domainName.toLowerCase()
        site.name = request.name.toLowerCase()
        dao.save(site)

        eventStream.publish(com.wutsi.site.event.EventType.UPDATED.urn, UpdatedEventPayload(id))

        return UpdateSiteResponse(
            siteId = id
        )
    }
}
