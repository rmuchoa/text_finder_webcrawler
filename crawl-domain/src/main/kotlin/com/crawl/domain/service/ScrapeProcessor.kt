package com.crawl.domain.service

import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.exception.UnfinishedWebScrapingException
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.values.Id
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class ScrapeProcessor(
    val repository: ScrapeRepositoryPort,
    val scrapeExecutor: ScrapeExecutor) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun loadAndProcess(scrapeId: Id) {

        log.info("PROCESSING: Loading scrape {} to execute scraping on it", scrapeId);
        val scrape = repository.load(scrapeId)

        log.info("PROCESSING: Starting to execute scraping on scrape: {}", scrape)
        val result: ScrapeResult = scrapeExecutor.executeFor(scrape)

        result.takeUnless { it.partialResult }?.also {
            log.warn("FULL RESULT: WebCrawler completely finished page navigation!!!")
        }?: run {
            log.warn("PARTIAL RESULT: WebCrawler doesn't finished page navigation yet!")
            throw UnfinishedWebScrapingException(
                message = "WebCrawler doesn't finished page navigation yet to reach a full result")
        }
    }

}