package com.crawl.application.notification

import com.crawl.domain.port.output.ScrapeNotificationDeliverPort

interface ScrapeNotificationDeliver:
    ScrapeNotificationDeliverPort {
    fun requeueUnfinishedScrape(notification: ScrapeNotification)
}