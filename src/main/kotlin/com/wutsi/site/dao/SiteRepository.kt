package com.wutsi.site.dao

import com.wutsi.site.entity.SiteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SiteRepository : CrudRepository<SiteEntity, Long>
