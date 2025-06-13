package com.crawl.infrastructure.repository

import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.port.output.CrawlRepositoryPort
import org.springframework.stereotype.Repository

@Repository
class CrawlRepository: CrawlRepositoryPort {
    override fun save(crawlIntention: CrawlIntention): CrawlIntention {
        return crawlIntention
    }
}