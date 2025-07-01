package com.crawl.application.port.input

import com.crawl.application.dto.ScrapeRequest
import com.crawl.application.dto.RequestedScrape

interface ScrapeInputPort {
    fun requestScrape(request: ScrapeRequest): RequestedScrape
}