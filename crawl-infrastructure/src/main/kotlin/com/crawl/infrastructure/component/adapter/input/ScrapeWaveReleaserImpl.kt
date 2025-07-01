package com.crawl.infrastructure.component.adapter.input

import com.crawl.application.adapter.input.ScrapeWaveReleaser
import com.crawl.application.notification.ScrapeNotificationDeliver
import com.crawl.domain.service.ScrapeProcessor
import org.springframework.stereotype.Service

@Service
class ScrapeWaveReleaserImpl(
    scrapeProcessor: ScrapeProcessor,
    notificationDeliver: ScrapeNotificationDeliver):
    ScrapeWaveReleaser(scrapeProcessor, notificationDeliver) {
}