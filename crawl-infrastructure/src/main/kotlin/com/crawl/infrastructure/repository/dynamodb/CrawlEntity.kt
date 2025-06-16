package com.crawl.infrastructure.repository.dynamodb

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
    var partialResult: Boolean = true,
    var scrapedUrls: List<String> = ArrayList()) {

    constructor() : this(id = "", status = "", keyword = "")

    companion object {
        fun of(crawlIntention: CrawlIntention) = of(
            crawlIntention.getId(),
            crawlIntention.status,
            crawlIntention.keyword)

        fun of(id: Id, status: CrawlStatus, keyword: Keyword) = CrawlEntity(
            id = id.toString(),
            status = status.toString(),
            keyword = keyword.toString())
    }

}
