package com.wutsi.site.dao

import com.wutsi.site.entity.AttributeEntity
import com.wutsi.site.entity.SiteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AttributeRepository : CrudRepository<AttributeEntity, Long> {
    fun findBySiteAndUrn(site: SiteEntity, urn: String): Optional<AttributeEntity>
}
