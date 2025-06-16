package com.crawl.infrastructure.repository

import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.infrastructure.repository.dynamodb.CrawlEntity
import com.crawl.infrastructure.repository.dynamodb.CrawlEntityDynamoDBRepository
import org.springframework.stereotype.Repository

@Repository
class CrawlRepository(
    val dynamoDBRepository: CrawlEntityDynamoDBRepository): CrawlRepositoryPort {

    override fun save(crawlIntention: CrawlIntention): CrawlIntention {

        val crawlEntity: CrawlEntity = CrawlEntity.of(crawlIntention)

        dynamoDBRepository.save(crawlEntity)

        return crawlIntention;
    }

}