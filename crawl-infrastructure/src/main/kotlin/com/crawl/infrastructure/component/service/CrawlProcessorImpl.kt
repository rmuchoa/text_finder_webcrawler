package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.service.CrawlProcessor
import com.crawl.domain.service.CrawlScrapeExecutor
import org.springframework.stereotype.Service

@Service
class CrawlProcessorImpl(
    repository: CrawlRepositoryPort,
    crawlScrapeExecutor: CrawlScrapeExecutor):
    CrawlProcessor(repository, crawlScrapeExecutor)