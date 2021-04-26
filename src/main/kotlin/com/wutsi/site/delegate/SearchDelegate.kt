package com.wutsi.site.`delegate`

import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.SearchSiteResponse
import com.wutsi.site.dto.SiteSummary
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
public class SearchDelegate(private val dao: SiteRepository) {
    public fun invoke(limit: Int = 20, offset: Int = 0): SearchSiteResponse {
        val sites = dao.findAll(
            PageRequest.of(offset / limit, limit)
        )
        return SearchSiteResponse(
            sites = sites.map {
                SiteSummary(
                    id = it.id ?: -1,
                    name = it.name,
                    displayName = it.displayName
                )
            }
        )
    }
}
