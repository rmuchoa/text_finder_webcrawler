package com.crawl.infrastructure.notification

import com.crawl.application.notification.CrawlNotification
import com.crawl.domain.values.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@ConsistentCopyVisibility
data class CrawlNotificationDTO internal constructor(
    val id: String,
    val retryCount: Int,
    val firstAttempt: LocalDateTime,
    val lastAttempt: LocalDateTime
) : CrawlNotification {

    constructor(id: Id) : this(
        id = id,
        retryCount = 0,
        firstAttempt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

    internal constructor(
        id: Id,
        retryCount: Int,
        firstAttempt: LocalDateTime
    ) : this(id = id.toString(), retryCount, firstAttempt, lastAttempt = firstAttempt)

    override fun getId() = Id.of(id)

    override fun retry() = copy(
        retryCount = retryCount+1,
        lastAttempt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

}
