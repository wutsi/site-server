package com.wutsi.site.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "T_SITE")
data class SiteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String = "",

    @Column(name = "display_name")
    var displayName: String = "",

    @Column(name = "domain_name")
    var domainName: String = "",

    var language: String = "",
    var currency: String = "",

    @OneToMany(mappedBy = "site")
    val attributes: List<AttributeEntity> = emptyList()
)
