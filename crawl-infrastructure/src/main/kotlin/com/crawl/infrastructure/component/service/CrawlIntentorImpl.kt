package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.service.CrawlIntentor
import org.springframework.stereotype.Service

@Service
class CrawlIntentorImpl(
    repository: CrawlRepositoryPort,
    deliver: CrawlNotificationDeliverPort) :
    CrawlIntentor(repository, deliver)