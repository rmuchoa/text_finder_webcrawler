package com.crawl.infrastructure.repository

import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.exception.CrawlNotFoundException
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.values.Id
import com.crawl.infrastructure.repository.dynamodb.CrawlEntity
import com.crawl.infrastructure.repository.dynamodb.CrawlEntityDynamoDBRepository
import org.springframework.stereotype.Repository

@Repository
class CrawlRepository(
    val dynamoDBRepository: CrawlEntityDynamoDBRepository):
    CrawlRepositoryPort {

    override fun save(crawlIntention: CrawlIntention): CrawlIntention {

        val crawlEntity = CrawlEntity.of(crawlIntention)

        dynamoDBRepository.save(crawlEntity)

        return crawlIntention;
    }

    override fun load(crawlId: Id): Crawl {

        val crawlEntity = dynamoDBRepository.findById(crawlId)

        return crawlEntity?.let {
            Crawl.of(
                id = it.id,
                status = it.status,
                keyword = it.keyword,
                partialResult = it.partialResult,
                scrapedUrls = it.scrapedUrls)
        } ?: throw CrawlNotFoundException(
            message = "crawl not found: $crawlId")
    }

    override fun save(crawl: Crawl): Crawl {

        val crawlEntity = CrawlEntity.of(crawl)

        dynamoDBRepository.save(crawlEntity)

        return crawl
    }

}