package com.crawl.infrastructure.notification

import com.crawl.domain.values.Id
import kotlinx.serialization.Serializable

@Serializable
@ConsistentCopyVisibility
data class CrawlNotification internal constructor(val id: String) {

    constructor(id: Id) : this(id = id.toString())

    fun getId() = Id.of(id)

}
