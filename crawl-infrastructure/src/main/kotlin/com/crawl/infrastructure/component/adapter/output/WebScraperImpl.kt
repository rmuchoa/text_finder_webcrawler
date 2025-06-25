package com.crawl.infrastructure.component.adapter.output

import com.crawl.application.adapter.output.WebScraper
import com.crawl.application.port.output.VisitedUrlRepositoryPort
import com.crawl.application.service.WebPageFinder
import org.springframework.stereotype.Service

@Service
class WebScraperImpl(
    webPageFinder: WebPageFinder,
    visitedUrlRepository: VisitedUrlRepositoryPort
) : WebScraper(webPageFinder, visitedUrlRepository)