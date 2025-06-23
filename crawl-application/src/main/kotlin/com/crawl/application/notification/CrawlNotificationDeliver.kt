package com.crawl.application.notification

import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.values.Id

interface CrawlNotificationDeliver:
    CrawlNotificationDeliverPort {
    fun requeueUnfinishedCrawl(id: Id)
}