package com.crawl.application.notification

import com.crawl.domain.port.output.CrawlNotificationDeliverPort

interface CrawlNotificationDeliver:
    CrawlNotificationDeliverPort {
    fun requeueUnfinishedCrawl(notification: CrawlNotification)
}