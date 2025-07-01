package com.crawl.domain.port.output

import com.crawl.domain.values.Id

interface ScrapeNotificationDeliverPort {
    fun notifyIntentedScrape(id: Id)
}