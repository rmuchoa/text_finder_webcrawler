package com.crawl.domain.service

import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.exception.UnfinishedWebCrawlingException
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.values.Id
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlProcessor(
    val repository: CrawlRepositoryPort,
    val crawlScrapeExecutor: CrawlScrapeExecutor) {

    var log: Logger = LoggerFactory.getLogger(CrawlProcessor::class.java)

    fun loadAndProcess(crawlId: Id) {

        log.info("PROCESSING: Loading crawl {} to execute scraping on", crawlId);
        val crawl = repository.load(crawlId)

        log.info("PROCESSING: Starting to execute scraping on crawl: {}", crawl)
        val result: CrawlResult = crawlScrapeExecutor.executeScrapeFor(crawl)

        if (!result.partialResult) return

        log.warn("PARTIAL RESULT: WebCrawler doesn't finished page navigation yet!")
        throw UnfinishedWebCrawlingException(
            message = "WebCrawler doesn't finished page navigation yet to reach a full result")

    }

}