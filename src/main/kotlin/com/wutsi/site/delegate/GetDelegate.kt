package com.wutsi.site.`delegate`

import com.wutsi.error.exception.NotFoundException
import com.wutsi.site.dao.SiteRepository
import com.wutsi.site.dto.Attribute
import com.wutsi.site.dto.GetSiteResponse
import com.wutsi.site.dto.Site
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
public class GetDelegate(
    private val dao: SiteRepository
) {
    @Cacheable(value = ["default"], key = "#id")
    public fun invoke(id: Long): GetSiteResponse {
        val site = dao.findById(id).orElseThrow { NotFoundException(id.toString()) }
        return GetSiteResponse(
            site = Site(
                id = site.id!!,
                name = site.name,
                displayName = site.displayName,
                domainName = site.domainName,
                websiteUrl = websiteUrl(site.domainName),
                attributes = site.attributes.map {
                    Attribute(
                        urn = it.urn,
                        value = it.value
                    )
                }
            )
        )
    }

    private fun websiteUrl(domainName: String): String =
        if (domainName == "localhost" || domainName == "127.0.0.1")
            "http://$domainName"
        else if (domainName.startsWith("www."))
            "https://$domainName"
        else
            "https://www.$domainName"
}
