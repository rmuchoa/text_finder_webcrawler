package com.crawl.domain.service

import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.port.output.CrawlRepositoryPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlIntentor(
    val repository: CrawlRepositoryPort,
    val deliver: CrawlNotificationDeliverPort) {

    var log: Logger = LoggerFactory.getLogger(CrawlIntentor::class.java)

    fun intent(crawlIntention: CrawlIntention): CrawlIntention {

        log.debug("CRAWL: Storing a crawl intention for keyword ${crawlIntention.keyword}! id: ${crawlIntention.id}")

        val intentedCrawl: CrawlIntention = repository.save(crawlIntention)
        log.info("CRAWL: Stored the crawl intention by id ${crawlIntention.id}!")

        deliver.notifyIntentedCrawl(intentedCrawl.getId())
        log.info("CRAWL: Notified the crawl intention id: ${crawlIntention.id}!")

        return intentedCrawl
    }
}