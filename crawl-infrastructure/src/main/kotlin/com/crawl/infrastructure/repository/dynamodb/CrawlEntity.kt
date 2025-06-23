package com.crawl.infrastructure.repository.dynamodb

import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class CrawlEntity(
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
        fun of(crawlIntention: CrawlIntention) = of(
            id = crawlIntention.getId(),
            status = crawlIntention.status,
            keyword = crawlIntention.keyword)

        fun of(crawl: Crawl) = of(
            id = crawl.id,
            status = crawl.status,
            keyword = crawl.keyword,
        )

        fun of(
            id: Id,
            status: CrawlStatus,
            keyword: Keyword) = CrawlEntity(
            id = id.toString(),
            status = status.toString(),
            keyword = keyword.toString(),
            partialResult = true,
            scrapedUrls = ArrayList())

        fun of(
            id: Id,
            status: CrawlStatus,
            keyword: Keyword,
            partialResult: Boolean,
            scrapedUrls: List<String>) = CrawlEntity(
            id = id.toString(),
            status = status.toString(),
            keyword = keyword.toString(),
            partialResult = partialResult,
            scrapedUrls = scrapedUrls)
    }

}
