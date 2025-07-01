package com.crawl.infrastructure.component.adapter.input

import com.crawl.application.adapter.input.ScrapeInputAdapter
import com.crawl.domain.service.ScrapeIntentor
import org.springframework.stereotype.Service

@Service
class ScrapeInputAdapterImpl(
    scrapeIntentor: ScrapeIntentor) :
    ScrapeInputAdapter(scrapeIntentor)