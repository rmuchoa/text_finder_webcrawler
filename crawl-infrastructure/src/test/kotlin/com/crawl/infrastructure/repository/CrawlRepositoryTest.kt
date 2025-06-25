package com.crawl.infrastructure.repository

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.exception.CrawlNotFoundException
import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import com.crawl.infrastructure.repository.dynamodb.CrawlEntity
import com.crawl.infrastructure.repository.dynamodb.CrawlEntityDynamoDBRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq

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
        val crawlIntention = CrawlIntention.of(keywordValue = defaultKeyword);

        repository.save(crawlIntention)

        verify(dynamoDBRepository, atLeastOnce()).save(crawlEntity = crawlEntityCaptor.capture())
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
                        equalTo(listOf<String>())))))
    }

    @Test
    @DisplayName("Deve lançar CrawlNotFoundException quando receber Crawl nulo ao chamar findById passando CrawlId para carregar Crawl")
    fun shouldThrowCrawlNotFoundExceptionWhenReceiveNullCrawlOnCallingFindByIdPassingCrawlIdToLoadCrawl() {
        val crawlId = Id.of(defaultId)
        val nullCrawlEntity: CrawlEntity? = null
        `when`(dynamoDBRepository.findById(crawlId = eq(crawlId))).thenReturn(nullCrawlEntity)

        val thrown: CrawlNotFoundException? = assertThrows(
            CrawlNotFoundException::class.java,
            Executable { repository.load(crawlId) })

        assertThat(thrown,
            hasProperty("message",
                equalTo("crawl not found: $crawlId")))
    }

    @Test
    @DisplayName("Deve pedir ao DynamoDBRepository para encontrar por id passando CrawlId quando carregar Crawl por CrawlId")
    fun shouldAskDynamoDBRepositoryToFindByIdPassingCrawlIdWhenLoadingCrawlByCrawlId() {
        val crawlId = Id.of(defaultId)
        val crawlEntity = CrawlEntity.of(
            id = crawlId,
            status = CrawlStatus.ACTIVE,
            keyword = Keyword.of(defaultKeyword),
            partialResult,
            scrapedUrls = listOf())
        `when`(dynamoDBRepository.findById(crawlId = eq(crawlId))).thenReturn(crawlEntity)

        repository.load(crawlId)

        verify(dynamoDBRepository, atLeastOnce()).findById(crawlId = eq(crawlId))
    }

    @Test
    @DisplayName("Deve pedir ao DynamoDBRepository para encontrar por id passando CrawlId quando carregar Crawl por CrawlId")
    fun shouldReturnCrawlBasedOnCrawlEntityObtainedOnCallingFindByIdWhenLoadCrawlByCrawlId() {
        val crawlId = Id.of(defaultId)
        val scrapedUrls = listOf(Url.of(url = myLink))
        val doneStatus = CrawlStatus.DONE
        val scrapingKeyword = Keyword.of(defaultKeyword)
        val crawlEntity = CrawlEntity.of(
            id = crawlId,
            status = doneStatus,
            keyword = scrapingKeyword,
            partialResult = notPartialResult,
            scrapedUrls)
        `when`(dynamoDBRepository.findById(crawlId = eq(crawlId))).thenReturn(crawlEntity)

        val result = repository.load(crawlId)

        assertThat(result, allOf(
            instanceOf(Crawl::class.java),
            hasProperty("id", instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", allOf(instanceOf<Keyword>(CrawlStatus::class.java), equalTo(doneStatus))),
            hasProperty("result", allOf(
                instanceOf<CrawlResult>(CrawlResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf(Boolean::class.java), equalTo(notPartialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls)))
            ))
        ))
    }

    @Test
    @DisplayName("Deve pedir ao repositório do DynamoDB para salvar um CrawlEntity baseado em um Crawl quando o repositório salvar dado Crawl")
    fun shouldAskDynamoDbRepositoryToSaveCrawlEntityBuiltBasedOnCrawlWhenRepositorySaveGivenCrawl() {
        val currentStatus = CrawlStatus.DONE.status
        val scrapedUrls: List<String> = listOf()
        val crawl = Crawl.of(
            id = defaultId,
            status = currentStatus,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls)

        repository.save(crawl)

        verify(dynamoDBRepository, atLeastOnce()).save(crawlEntity = crawlEntityCaptor.capture())
        assertThat(crawlEntityCaptor.firstValue,
            allOf(
                instanceOf(CrawlEntity::class.java),
                hasProperty("id",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(defaultId))),
                hasProperty("status",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(currentStatus))),
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
                        equalTo(scrapedUrls)))))
    }

}