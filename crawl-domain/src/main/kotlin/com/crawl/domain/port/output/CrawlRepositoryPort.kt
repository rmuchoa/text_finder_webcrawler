package com.crawl.domain.port.output

import com.crawl.domain.entity.CrawlIntention

interface CrawlRepositoryPort {
    fun save(crawlIntention: CrawlIntention): CrawlIntention
}