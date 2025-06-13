package com.crawl.infrastructure.notification

import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.values.Id
import org.springframework.stereotype.Repository

@Repository
class CrawlNotificationDeliverBroker: CrawlNotificationDeliverPort {
    override fun notifyIntentedCrawl(id: Id) {

    }
}