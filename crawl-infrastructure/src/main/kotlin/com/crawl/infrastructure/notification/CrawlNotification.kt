package com.crawl.infrastructure.notification

import com.crawl.domain.values.Id
import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class CrawlNotification internal constructor(val id: String) {

    constructor(id: Id) : this(id.toString())
}
