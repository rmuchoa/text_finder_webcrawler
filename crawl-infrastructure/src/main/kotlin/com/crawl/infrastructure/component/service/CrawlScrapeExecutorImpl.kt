package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.service.CrawlScrapeExecutor
import org.springframework.stereotype.Service

@Service
class CrawlScrapeExecutorImpl(
    repository: CrawlRepositoryPort,
    webScraper: WebScraperPort):
    CrawlScrapeExecutor(repository, webScraper)