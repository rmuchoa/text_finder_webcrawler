package com.crawl.domain.port.output

import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.values.Id

interface CrawlRepositoryPort {
    fun save(crawlIntention: CrawlIntention): CrawlIntention
    fun load(crawlId: Id): Crawl
    fun save(crawl: Crawl): Crawl
}