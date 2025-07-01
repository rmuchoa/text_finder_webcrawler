package com.crawl.infrastructure.repository.dynamodb

import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class ScrapeEntity(
    @get:DynamoDbPartitionKey var id: String,
    var status: String,
    var keyword: String,
    var partialResult: Boolean,
    var scrapedUrls: List<String>) {

    constructor() : this(
        id = "",
        status = "",
        keyword = "",
        partialResult = true,
        scrapedUrls = ArrayList())

    companion object {
        fun of(scrapeIntention: ScrapeIntention) = of(
            id = scrapeIntention.getId(),
            status = scrapeIntention.status,
            keyword = scrapeIntention.keyword)

        fun of(scrape: Scrape) = of(
            id = scrape.id,
            status = scrape.status,
            keyword = scrape.keyword,
        )

        fun of(
            id: Id,
            status: ScrapeStatus,
            keyword: Keyword) = ScrapeEntity(
            id = id.toString(),
            status = status.toString(),
            keyword = keyword.toString(),
            partialResult = true,
            scrapedUrls = ArrayList())

        fun of(
            id: Id,
            status: ScrapeStatus,
            keyword: Keyword,
            partialResult: Boolean,
            scrapedUrls: List<Url>) = ScrapeEntity(
            id = id.toString(),
            status = status.toString(),
            keyword = keyword.toString(),
            partialResult = partialResult,
            scrapedUrls = scrapedUrls.map { it.url })
    }

}
