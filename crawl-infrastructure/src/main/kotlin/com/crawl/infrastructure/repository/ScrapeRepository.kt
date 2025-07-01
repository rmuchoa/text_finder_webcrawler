package com.crawl.infrastructure.repository

import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.exception.ScrapeNotFoundException
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.values.Id
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntity
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntityDynamoDBRepository
import org.springframework.stereotype.Repository

@Repository
class ScrapeRepository(
    val dynamoDBRepository: ScrapeEntityDynamoDBRepository):
    ScrapeRepositoryPort {

    override fun save(scrapeIntention: ScrapeIntention): ScrapeIntention {

        val scrapeEntity = ScrapeEntity.of(scrapeIntention)

        dynamoDBRepository.save(scrapeEntity)

        return scrapeIntention;
    }

    override fun load(scrapeId: Id): Scrape {

        val scrapeEntity = dynamoDBRepository.findById(scrapeId)

        return scrapeEntity?.let {
            Scrape.of(
                id = it.id,
                status = it.status,
                keyword = it.keyword,
                partialResult = it.partialResult,
                scrapedUrls = it.scrapedUrls)
        } ?: throw ScrapeNotFoundException(
            message = "scrape not found: $scrapeId")
    }

    override fun save(scrape: Scrape): Scrape {

        val scrapeEntity = ScrapeEntity.of(scrape)

        dynamoDBRepository.save(scrapeEntity)

        return scrape
    }

}