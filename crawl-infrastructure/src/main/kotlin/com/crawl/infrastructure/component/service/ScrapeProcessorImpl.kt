package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.service.ScrapeProcessor
import com.crawl.domain.service.ScrapeExecutor
import org.springframework.stereotype.Service

@Service
class ScrapeProcessorImpl(
    repository: ScrapeRepositoryPort,
    scrapeExecutor: ScrapeExecutor):
    ScrapeProcessor(repository, scrapeExecutor)