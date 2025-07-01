package com.crawl.domain.service

import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.port.output.ScrapeNotificationDeliverPort
import com.crawl.domain.port.output.ScrapeRepositoryPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class ScrapeIntentor(
    val repository: ScrapeRepositoryPort,
    val deliver: ScrapeNotificationDeliverPort) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun intent(scrapeIntention: ScrapeIntention): ScrapeIntention {

        log.debug("SCRAPE: Storing a scrape intention for keyword ${scrapeIntention.keyword}! id: ${scrapeIntention.id}")

        val intentedScrape: ScrapeIntention = repository.save(scrapeIntention)
        log.info("SCRAPE: Stored the scrape intention by id ${scrapeIntention.id}!")

        deliver.notifyIntentedScrape(id = intentedScrape.getId())
        log.info("SCRAPE: Notified the scrape intention id: ${scrapeIntention.id}!")

        return intentedScrape
    }
}