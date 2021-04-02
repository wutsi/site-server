package com.wutsi.site.`delegate`

import com.wutsi.site.dao.AttributeRepository
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.SetAttributeRequest
import com.wutsi.site.entity.AttributeEntity
import com.wutsi.site.event.EventType.UPDATED
import com.wutsi.site.event.UpdatedEventPayload
import com.wutsi.stream.EventStream
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
public class SetAttributeDelegate(
    private val siteDao: SiteRepository,
    private val attributeDao: AttributeRepository,
    private val eventStream: EventStream
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(UpdateDelegate::class.java)
    }

    @CacheEvict(value = ["default"], key = "#id")
    public fun invoke(
        id: Long,
        urn: String,
        request: SetAttributeRequest
    ) {
        save(id, urn, request)
        eventStream.publish(UPDATED.urn, UpdatedEventPayload(id, urn))
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
