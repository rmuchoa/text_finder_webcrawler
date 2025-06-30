package com.crawl.domain.service

import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlScrapeExecutor(
    val repository: CrawlRepositoryPort,
    val webScraper: WebScraperPort) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun executeScrapeFor(crawl: Crawl): CrawlResult {

        log.info("EXECUTION: Starting to scrape crawl {} by keyword {}!", crawl.id, crawl.keyword)
        val result = webScraper.startKeywordScraping(keyword = crawl.keyword)

        log.info("EXECUTION: Received result from scraping by keyword {} with status {}!", crawl.keyword, crawl.status)
        crawl.applyResults(result)

        repository.save(crawl)
        log.debug("EXECUTION: Saved crawl with result!")

        return result
    }

}