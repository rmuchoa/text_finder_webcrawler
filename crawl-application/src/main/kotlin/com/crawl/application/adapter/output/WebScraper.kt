package com.crawl.application.adapter.output

import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.values.Keyword
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebScraper: WebScraperPort {

    val log: Logger = LoggerFactory.getLogger(WebScraper::class.java)

    override fun startKeywordScraping(keyword: Keyword): CrawlResult {
        TODO("Not yet implemented")
    }

}