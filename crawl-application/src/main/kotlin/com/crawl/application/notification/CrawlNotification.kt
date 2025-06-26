package com.crawl.application.notification

import com.crawl.domain.values.Id

interface CrawlNotification {
    fun getId(): Id
    fun retry(): CrawlNotification
}