package com.crawl.application.adapter.input

import com.crawl.application.notification.CrawlNotification
import com.crawl.application.notification.CrawlNotificationDeliver
import com.crawl.application.port.input.CrawlWaveReleaserPort
import com.crawl.domain.exception.UnfinishedWebCrawlingException
import com.crawl.domain.service.CrawlProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlWaveReleaser(
    val crawlProcessor: CrawlProcessor,
    val notificationDeliver: CrawlNotificationDeliver
) : CrawlWaveReleaserPort {

    var log: Logger = LoggerFactory.getLogger(CrawlWaveReleaser::class.java)

    override fun releaseWave(notification: CrawlNotification) {
        runCatching {
            log.info("CRAWL WAVE: New wave released to load and process crawl {}.", notification.getId())
            crawlProcessor.loadAndProcess(crawlId = notification.getId())
        }.onFailure {
            treatFailures(throwable = it, notification)
        }
    }

    fun treatFailures(throwable: Throwable, notification: CrawlNotification) {
        throwable.takeIf { it is UnfinishedWebCrawlingException }?.run {
            log.warn("UNFINISHED: Found crawl needs at least once more rescraping to get full result! message: {}", message)
            notificationDeliver.requeueUnfinishedCrawl(notification)
        }

        throwable.takeUnless { it is UnfinishedWebCrawlingException }?.run {
            log.error("CRAWL WAVE: Some problem occurred on waving crawl {} process. {}", notification.getId(), message)
        }
    }

}