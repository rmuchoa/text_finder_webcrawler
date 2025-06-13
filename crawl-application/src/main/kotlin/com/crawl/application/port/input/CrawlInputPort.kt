package com.crawl.application.port.input

import com.crawl.application.dto.CrawlRequest
import com.crawl.application.dto.RequestedCrawl

interface CrawlInputPort {
    fun requestCrawl(request: CrawlRequest): RequestedCrawl
}