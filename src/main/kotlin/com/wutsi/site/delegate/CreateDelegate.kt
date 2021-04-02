package com.wutsi.site.`delegate`

import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.CreateSiteRequest
import com.wutsi.site.dto.CreateSiteResponse
import com.wutsi.site.entity.SiteEntity
import com.wutsi.site.event.CreatedEventPayload
import com.wutsi.site.event.EventType
import com.wutsi.stream.EventStream
import org.springframework.stereotype.Service

@Service
public class CreateDelegate(
    private val dao: SiteRepository,
    private val eventStream: EventStream
) {
    public fun invoke(request: CreateSiteRequest): CreateSiteResponse {
        val site = dao.save(
            SiteEntity(
                name = request.name.toLowerCase(),
                displayName = request.displayName,
                domainName = request.domainName.toLowerCase()
            )
        )

        eventStream.publish(EventType.CREATED.urn, CreatedEventPayload(site.id!!))

        return CreateSiteResponse(
            siteId = site.id
        )
    }
}
