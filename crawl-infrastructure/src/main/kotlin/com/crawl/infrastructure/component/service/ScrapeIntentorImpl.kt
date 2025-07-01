package com.crawl.infrastructure.component.service

import com.crawl.domain.port.output.ScrapeNotificationDeliverPort
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.service.ScrapeIntentor
import org.springframework.stereotype.Service

@Service
class ScrapeIntentorImpl(
    repository: ScrapeRepositoryPort,
    deliver: ScrapeNotificationDeliverPort) :
    ScrapeIntentor(repository, deliver)