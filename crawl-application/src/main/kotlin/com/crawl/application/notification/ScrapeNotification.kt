package com.crawl.application.notification

import com.crawl.domain.values.Id

interface ScrapeNotification {
    fun getId(): Id
    fun retry(): ScrapeNotification
}