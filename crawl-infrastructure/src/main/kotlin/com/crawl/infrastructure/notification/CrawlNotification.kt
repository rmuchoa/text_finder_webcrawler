package com.crawl.infrastructure.notification

import com.crawl.domain.values.Id
import kotlinx.serialization.Serializable

@Serializable
data class CrawlNotification(val id: Id)
