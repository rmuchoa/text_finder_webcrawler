package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.service.ScrapeExecutor
import org.springframework.stereotype.Service

@Service
class ScrapeExecutorImpl(
    repository: ScrapeRepositoryPort,
    webScraper: WebScraperPort):
    ScrapeExecutor(repository, webScraper)