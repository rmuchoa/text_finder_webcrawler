package com.crawl.infrastructure.component.adapter.input

import com.crawl.application.adapter.input.CrawlInputAdapter
import com.crawl.domain.service.CrawlIntentor
import org.springframework.stereotype.Service

@Service
class CrawlInputAdapterImpl(
    crawlIntentor: CrawlIntentor) :
    CrawlInputAdapter(crawlIntentor)