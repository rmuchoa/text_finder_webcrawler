package com.crawl.infrastructure.repository

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.exception.ScrapeNotFoundException
import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntity
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntityDynamoDBRepository
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
class ScrapeRepositoryTest : AbstractTest() {

    @Mock private lateinit var dynamoDBRepository: ScrapeEntityDynamoDBRepository
    private val scrapeEntityCaptor = argumentCaptor<ScrapeEntity>()
    private lateinit var repository: ScrapeRepository

    @BeforeEach
    fun setUp() {
        repository = ScrapeRepository(dynamoDBRepository)
    }

    @Test
    @DisplayName("Deve pedir ao repositório do DynamoDB para salvar um ScrapeEntity baseado em um ScrapeIntention quando o repositório salvar uma Intention")
    fun shouldAskDynamoDbRepositoryToSaveScrapeEntityBuiltBasedOnScrapeIntentionWhenRepositorySaveIntention() {
        val scrapeIntention = ScrapeIntention.of(keywordValue = defaultKeyword);

        repository.save(scrapeIntention)

        verify(dynamoDBRepository, atLeastOnce()).save(scrapeEntity = scrapeEntityCaptor.capture())
        assertThat(scrapeEntityCaptor.firstValue,
            allOf(
                instanceOf(ScrapeEntity::class.java),
                hasProperty("id",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(scrapeIntention.getId().toString()))),
                hasProperty("status",
                    allOf(
                        instanceOf(String::class.java),
                        equalTo(scrapeIntention.status.toString()))),
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
    @DisplayName("Deve lançar ScrapeNotFoundException quando receber Scrape nulo ao chamar findById passando ScrapeId para carregar Scrape")
    fun shouldThrowScrapeNotFoundExceptionWhenReceiveNullScrapeOnCallingFindByIdPassingScrapeIdToLoadScrape() {
        val scrapeId = Id.of(defaultId)
        val nullScrapeEntity: ScrapeEntity? = null
        `when`(dynamoDBRepository.findById(scrapeId = eq(scrapeId))).thenReturn(nullScrapeEntity)

        val thrown: ScrapeNotFoundException? = assertThrows(
            ScrapeNotFoundException::class.java,
            Executable { repository.load(scrapeId) })

        assertThat(thrown,
            hasProperty("message",
                equalTo("scrape not found: $scrapeId")))
    }

    @Test
    @DisplayName("Deve pedir ao DynamoDBRepository para encontrar por id passando ScrapeId quando carregar Scrape por ScrapeId")
    fun shouldAskDynamoDBRepositoryToFindByIdPassingScrapeIdWhenLoadingScrapeByScrapeId() {
        val scrapeId = Id.of(defaultId)
        val scrapeEntity = ScrapeEntity.of(
            id = scrapeId,
            status = ScrapeStatus.ACTIVE,
            keyword = Keyword.of(defaultKeyword),
            partialResult,
            scrapedUrls = listOf())
        `when`(dynamoDBRepository.findById(scrapeId = eq(scrapeId))).thenReturn(scrapeEntity)

        repository.load(scrapeId)

        verify(dynamoDBRepository, atLeastOnce()).findById(scrapeId = eq(scrapeId))
    }

    @Test
    @DisplayName("Deve pedir ao DynamoDBRepository para encontrar por id passando ScrapeId quando carregar Scrape por ScrapeId")
    fun shouldReturnScrapeBasedOnScrapeEntityObtainedOnCallingFindByIdWhenLoadScrapeByScrapeId() {
        val scrapeId = Id.of(defaultId)
        val scrapedUrls = listOf(Url.of(url = myLink))
        val doneStatus = ScrapeStatus.DONE
        val scrapingKeyword = Keyword.of(defaultKeyword)
        val scrapeEntity = ScrapeEntity.of(
            id = scrapeId,
            status = doneStatus,
            keyword = scrapingKeyword,
            partialResult = notPartialResult,
            scrapedUrls)
        `when`(dynamoDBRepository.findById(scrapeId = eq(scrapeId))).thenReturn(scrapeEntity)

        val result = repository.load(scrapeId)

        assertThat(result, allOf(
            instanceOf(Scrape::class.java),
            hasProperty("id", instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", allOf(instanceOf<Keyword>(ScrapeStatus::class.java), equalTo(doneStatus))),
            hasProperty("result", allOf(
                instanceOf<ScrapeResult>(ScrapeResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf(Boolean::class.java), equalTo(notPartialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls)))
            ))
        ))
    }

    @Test
    @DisplayName("Deve pedir ao repositório do DynamoDB para salvar um ScrapeEntity baseado em um Scrape quando o repositório salvar dado Scrape")
    fun shouldAskDynamoDbRepositoryToSaveScrapeEntityBuiltBasedOnScrapeWhenRepositorySaveGivenScrape() {
        val currentStatus = ScrapeStatus.DONE.status
        val scrapedUrls: List<String> = listOf()
        val scrape = Scrape.of(
            id = defaultId,
            status = currentStatus,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls)

        repository.save(scrape)

        verify(dynamoDBRepository, atLeastOnce()).save(scrapeEntity = scrapeEntityCaptor.capture())
        assertThat(scrapeEntityCaptor.firstValue,
            allOf(
                instanceOf(ScrapeEntity::class.java),
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