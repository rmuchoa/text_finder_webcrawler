package com.crawl.domain.port.output

import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.values.Id

interface ScrapeRepositoryPort {
    fun save(scrapeIntention: ScrapeIntention): ScrapeIntention
    fun load(scrapeId: Id): Scrape
    fun save(scrape: Scrape): Scrape
}