package com.wutsi.site.`delegate`

import com.wutsi.site.dao.AttributeRepository
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.SetAttributeRequest
import com.wutsi.site.entity.AttributeEntity
import com.wutsi.site.event.SiteEventPayload
import com.wutsi.site.event.SiteEventType.SITE_UPDATED
import com.wutsi.stream.EventStream
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
public class SetAttributeDelegate(
    private val siteDao: SiteRepository,
    private val attributeDao: AttributeRepository,
    private val eventStream: EventStream
) {
    @CacheEvict(value = ["default"], key = "#id")
    public fun invoke(
        id: Long,
        urn: String,
        request: SetAttributeRequest
    ) {
        save(id, urn, request)
        eventStream.publish(SITE_UPDATED.urn, SiteEventPayload(id))
    }

    private fun save(id: Long, urn: String, request: SetAttributeRequest) {
        val site = siteDao.findById(id).get()
        val opt = attributeDao.findBySiteAndUrn(site, urn)

        val value = request.value
        if (value.isNullOrEmpty()) {
            if (opt.isPresent)
                attributeDao.delete(opt.get())
        } else {
            if (opt.isPresent) {
                val attribute = opt.get()
                attribute.value = value
                attributeDao.save(attribute)
            } else {
                attributeDao.save(
                    AttributeEntity(
                        site = site,
                        urn = urn,
                        value = value
                    )
                )
            }
        }
    }
}
