package com.crawl.domain.port.output

import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.values.Keyword

interface WebScraperPort {
    fun startKeywordScraping(keyword: Keyword): CrawlResult
}