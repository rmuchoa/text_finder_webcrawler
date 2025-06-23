package com.crawl.infrastructure.component.adapter.input

import com.crawl.application.adapter.input.CrawlWaveReleaser
import com.crawl.application.notification.CrawlNotificationDeliver
import com.crawl.domain.service.CrawlProcessor
import org.springframework.stereotype.Service

@Service
class CrawlWaveReleaserImpl(
    crawlProcessor: CrawlProcessor,
    notificationDeliver: CrawlNotificationDeliver):
    CrawlWaveReleaser(crawlProcessor, notificationDeliver) {
}