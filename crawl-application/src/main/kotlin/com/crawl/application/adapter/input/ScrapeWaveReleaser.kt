package com.crawl.application.adapter.input

import com.crawl.application.notification.ScrapeNotification
import com.crawl.application.notification.ScrapeNotificationDeliver
import com.crawl.application.port.input.ScrapeWaveReleaserPort
import com.crawl.domain.exception.UnfinishedWebScrapingException
import com.crawl.domain.service.ScrapeProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class ScrapeWaveReleaser(
    val scrapeProcessor: ScrapeProcessor,
    val notificationDeliver: ScrapeNotificationDeliver
) : ScrapeWaveReleaserPort {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun releaseWave(notification: ScrapeNotification) {
        runCatching {
            log.info("SCRAPE WAVE: New wave released to load and process crawl {}.", notification.getId())
            scrapeProcessor.loadAndProcess(scrapeId = notification.getId())
        }.onFailure {
            treatFailures(throwable = it, notification)
        }
    }

    fun treatFailures(throwable: Throwable, notification: ScrapeNotification) {
        throwable.takeIf { it is UnfinishedWebScrapingException }?.run {
            log.warn("UNFINISHED: Found scrape needs at least once more rescraping to get full result! message: {}", message)
            notificationDeliver.requeueUnfinishedScrape(notification)
        }

        throwable.takeUnless { it is UnfinishedWebScrapingException }?.run {
            log.error("SCRAPE WAVE: Some problem occurred on waving scrape {} process. {}", notification.getId(), message)
        }
    }

}