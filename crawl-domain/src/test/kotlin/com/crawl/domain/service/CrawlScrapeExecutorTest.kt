package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Crawl
import com.crawl.domain.entity.CrawlResult
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
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
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class CrawlScrapeExecutorTest : AbstractTest() {

    @Mock private lateinit var repository: CrawlRepositoryPort
    @Mock private lateinit var webScraper: WebScraperPort
    private val crawlCaptor = argumentCaptor<Crawl>()
    private lateinit var crawlScrapeExecutor: CrawlScrapeExecutor

    @BeforeEach
    fun setUp() {
        crawlScrapeExecutor = CrawlScrapeExecutor(repository, webScraper)
    }

    @Test
    @DisplayName("Deve pedir ao WebScraper para iniciar scraping a partir da Keyword do Crawl quando executar scrape para um dado Crawl")
    fun shouldAskWebScraperToStartKeywordScrapingWhenExecutingScrapeForGivenCrawl() {
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = listOf())
        `when`(webScraper.startKeywordScraping(keyword = eq(crawl.keyword))).thenReturn(crawl.result)

        crawlScrapeExecutor.executeScrapeFor(crawl)

        verify(webScraper, atLeastOnce()).startKeywordScraping(keyword = eq(Keyword.of(defaultKeyword)))
    }

    @Test
    @DisplayName("Deve pedir ao CrawlRepository para salvar Crawl com novo CrawlResult recebido quando executar scrape para um dado Crawl")
    fun shouldAskCrawlRepositoryToSaveCrawlWithReceivedNewResultWhenExecutingScrapeForThatCrawl() {
        val scrapedUrls: List<String> = listOf()
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(crawl.keyword))).thenReturn(crawl.result)

        crawlScrapeExecutor.executeScrapeFor(crawl)

        verify(repository, atLeastOnce()).save(crawl = crawlCaptor.capture())

        assertThat(crawlCaptor.firstValue,allOf(
            instanceOf(Crawl::class.java),
            hasProperty("id",instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", equalTo(CrawlStatus.ACTIVE)),
            hasProperty("result", allOf(
                instanceOf<CrawlResult>(CrawlResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf<Boolean>(Boolean::class.java), equalTo(partialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls.map { Url.of(url = it) })))
            ))))
    }

    @Test
    @DisplayName("Deve aplicar novo CrawlResult no Crawl e mudar status para DONE quando receber result do scrape iniciado para um Crawl e não for um result parcial ao executar scrape para esse Crawl")
    fun shouldApplyNewCrawlResultOnCrawlAndChangeStatusToDoneWhenReceiveResultFromStartedScrapeForCrawlAndWasNotPartialResultOnExecutingScrapeForThatCrawl() {
        val scrapedStringUrls: List<String> = listOf()
        val scrapedUrls: List<Url> = scrapedStringUrls.map { Url.of(url = it) }
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = scrapedStringUrls)
        val newCrawlResult = CrawlResult.of(keyword = crawl.keyword, notPartialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(crawl.keyword))).thenReturn(newCrawlResult)

        crawlScrapeExecutor.executeScrapeFor(crawl)

        verify(repository, atLeastOnce()).save(crawl = crawlCaptor.capture())

        assertThat(crawlCaptor.firstValue,allOf(
            instanceOf(Crawl::class.java),
            hasProperty("id",instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", equalTo(CrawlStatus.DONE)),
            hasProperty("result", allOf(
                instanceOf<CrawlResult>(CrawlResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf(Boolean::class.java), equalTo(notPartialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls)))
            ))))
    }

    @Test
    @DisplayName("Deve retornar CrawlResult obtido a partir da scrape da Keyword iniciada quando executando scrape para um Crawl")
    fun shouldReturnCrawlResultObtainedFromStartedKeywordScrapingWhenExecutingScrapeForCrawl() {
        val scrapedStringUrls: List<String> = listOf()
        val scrapedUrls: List<Url> = scrapedStringUrls.map { Url.of(url = it) }
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = scrapedStringUrls)
        val newCrawlResult = CrawlResult.of(keyword = crawl.keyword, notPartialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(crawl.keyword))).thenReturn(newCrawlResult)

        val result = crawlScrapeExecutor.executeScrapeFor(crawl)

        assertThat(result, allOf(
            instanceOf(CrawlResult::class.java),
            equalTo(newCrawlResult)))
    }

}