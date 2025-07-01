package com.crawl.domain.service

import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class ScrapeExecutor(
    val repository: ScrapeRepositoryPort,
    val webScraper: WebScraperPort) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun executeFor(scrape: Scrape): ScrapeResult {

        log.info("EXECUTION: Starting to scrape {} by keyword {}!", scrape.id, scrape.keyword)
        val result = webScraper.startKeywordScraping(keyword = scrape.keyword)

        log.info("EXECUTION: Received result from scraping by keyword {} with status {}!", scrape.keyword, scrape.status)
        scrape.applyResults(result)

        repository.save(scrape)
        log.debug("EXECUTION: Saved scrape with result!")

        return result
    }

}