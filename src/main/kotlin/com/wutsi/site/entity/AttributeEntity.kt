package com.wutsi.site.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "T_ATTRIBUTE")
data class AttributeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val urn: String = "",

    var value: String = "",

    @ManyToOne
    @JoinColumn(name = "site_fk")
    val site: SiteEntity = SiteEntity()
)
