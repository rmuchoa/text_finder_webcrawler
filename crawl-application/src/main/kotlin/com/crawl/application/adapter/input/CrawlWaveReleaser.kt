package com.crawl.application.adapter.input

import com.crawl.application.notification.CrawlNotificationDeliver
import com.crawl.application.port.input.CrawlWaveReleaserPort
import com.crawl.domain.exception.UnfinishedWebCrawlingException
import com.crawl.domain.service.CrawlProcessor
import com.crawl.domain.values.Id
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlWaveReleaser(
    val crawlProcessor: CrawlProcessor,
    val notificationDeliver: CrawlNotificationDeliver
) : CrawlWaveReleaserPort {

    var log: Logger = LoggerFactory.getLogger(CrawlWaveReleaser::class.java)

    override fun releaseWave(crawlId: Id) {
        runCatching {
            log.info("CRAWL WAVE: New wave released to load and process crawl {}.", crawlId)
            crawlProcessor.loadAndProcess(crawlId)
        }.onFailure {
            treatFailures(throwable = it, crawlId)
        }
    }

    fun treatFailures(throwable: Throwable, crawlId: Id) {
        throwable.takeIf { it is UnfinishedWebCrawlingException }?.run {
            log.warn("UNFINISHED: Found crawl needs at least once more rescraping to get full result! message: {}", message)
            notificationDeliver.requeueUnfinishedCrawl(crawlId)
        }

        throwable.takeUnless { it is UnfinishedWebCrawlingException }?.run {
            log.error("CRAWL WAVE: Some problem occurred on waving crawl {} process. {}", crawlId, message)
        }
    }

}