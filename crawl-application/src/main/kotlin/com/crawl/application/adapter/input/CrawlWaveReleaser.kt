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
    val notificationDeliver: CrawlNotificationDeliver):
    CrawlWaveReleaserPort {

    var log: Logger = LoggerFactory.getLogger(CrawlWaveReleaser::class.java)

    override fun releaseWave(crawlId: Id) {
        try {

            crawlProcessor.loadAndProcess(crawlId)

        } catch (exception: UnfinishedWebCrawlingException) {

            log.warn("UNFINISHED: Found crawl needs at least once more rescraping to get full result! message: {}", exception.message)
            notificationDeliver.requeueUnfinishedCrawl(crawlId)
        }
    }

}