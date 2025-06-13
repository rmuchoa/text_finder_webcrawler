package com.crawl.domain.port.output

import com.crawl.domain.values.Id

interface CrawlNotificationDeliverPort {
    fun notifyIntentedCrawl(id: Id)
}