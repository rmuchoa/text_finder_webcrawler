package com.crawl.application.adapter.input

import com.crawl.application.dto.ScrapeRequest
import com.crawl.application.dto.RequestedScrape
import com.crawl.application.port.input.ScrapeInputPort
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.service.ScrapeIntentor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class ScrapeInputAdapter(
    val scrapeIntentor: ScrapeIntentor):
    ScrapeInputPort {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun requestScrape(request: ScrapeRequest): RequestedScrape {

        log.debug("SCRAPE: Treating a scrape requisition by keyword ${request.keyword}!")

        val scrapeIntention: ScrapeIntention = ScrapeIntention.of(keywordValue = request.keyword)
        log.info("SCRAPE: Created a scrape intention with id ${scrapeIntention.id}! keyword: ${scrapeIntention.keyword}")

        val startedScrape: ScrapeIntention = scrapeIntentor.intent(scrapeIntention)
        log.debug("SCRAPE: Registered a scrape requisition by keyword ${request.keyword}!")

        return RequestedScrape.of(id = startedScrape.getId());
    }

}