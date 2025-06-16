package com.crawl.infrastructure.repository

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.CrawlIntention
import com.crawl.infrastructure.repository.dynamodb.CrawlEntity
import com.crawl.infrastructure.repository.dynamodb.CrawlEntityDynamoDBRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import java.util.Arrays

@ExtendWith(MockitoExtension::class)
class CrawlRepositoryTest : AbstractTest() {

    @Mock private lateinit var dynamoDBRepository: CrawlEntityDynamoDBRepository
    private val crawlEntityCaptor = argumentCaptor<CrawlEntity>()
    private lateinit var repository: CrawlRepository

    @BeforeEach
    fun setUp() {
        repository = CrawlRepository(dynamoDBRepository)
    }

    @Test
    @DisplayName("Deve pedir ao repositório do DynamoDB para salvar um CrawlEntity baseado em um CrawlIntention quando o repositório salvar uma Intention")
    fun shouldAskDynamoDbRepositoryToSaveCrawlEntityBuiltBasedOnCrawlIntentionWhenRepositorySaveIntention() {
        val crawlIntention = CrawlIntention.Companion.of(defaultKeyword);

        repository.save(crawlIntention)

        verify(dynamoDBRepository, atLeastOnce()).save(crawlEntityCaptor.capture())
        assertThat(crawlEntityCaptor.firstValue,
            allOf(
                instanceOf(CrawlEntity::class.java),
                hasProperty("id",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(crawlIntention.getId().toString()))),
                hasProperty("status",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(crawlIntention.status.toString()))),
                hasProperty("keyword",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(defaultKeyword))),
                hasProperty("partialResult",
                    allOf(
                        instanceOf(Boolean::class.java),
                        equalTo(partialResult))),
                hasProperty("scrapedUrls",
                    allOf<Any?>(
                        instanceOf(List::class.java),
                        equalTo(Arrays.asList<String>())))))
    }

}