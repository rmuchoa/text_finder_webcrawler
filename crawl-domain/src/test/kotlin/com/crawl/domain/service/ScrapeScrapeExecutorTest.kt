package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Scrape
import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.values.ScrapeStatus
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
class ScrapeScrapeExecutorTest : AbstractTest() {

    @Mock private lateinit var repository: ScrapeRepositoryPort
    @Mock private lateinit var webScraper: WebScraperPort
    private val scrapeCaptor = argumentCaptor<Scrape>()
    private lateinit var scrapeExecutor: ScrapeExecutor

    @BeforeEach
    fun setUp() {
        scrapeExecutor = ScrapeExecutor(repository, webScraper)
    }

    @Test
    @DisplayName("Deve pedir ao WebScraper para iniciar scraping a partir da Keyword do Scrape quando executar scrape para um dado Scrape")
    fun shouldAskWebScraperToStartKeywordScrapingWhenExecutingScrapeForGivenScrape() {
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = listOf())
        `when`(webScraper.startKeywordScraping(keyword = eq(scrape.keyword))).thenReturn(scrape.result)

        scrapeExecutor.executeFor(scrape)

        verify(webScraper, atLeastOnce()).startKeywordScraping(keyword = eq(Keyword.of(defaultKeyword)))
    }

    @Test
    @DisplayName("Deve pedir ao ScrapeRepository para salvar Scrape com novo ScrapeResult recebido quando executar scrape para um dado Scrape")
    fun shouldAskScrapeRepositoryToSaveScrapeWithReceivedNewResultWhenExecutingScrapeForThatScrape() {
        val scrapedUrls: List<String> = listOf()
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(scrape.keyword))).thenReturn(scrape.result)

        scrapeExecutor.executeFor(scrape)

        verify(repository, atLeastOnce()).save(scrape = scrapeCaptor.capture())

        assertThat(scrapeCaptor.firstValue,allOf(
            instanceOf(Scrape::class.java),
            hasProperty("id",instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", equalTo(ScrapeStatus.ACTIVE)),
            hasProperty("result", allOf(
                instanceOf<ScrapeResult>(ScrapeResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf<Boolean>(Boolean::class.java), equalTo(partialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls.map { Url.of(url = it) })))
            ))))
    }

    @Test
    @DisplayName("Deve aplicar novo ScrapeResult no Scrape e mudar status para DONE quando receber result do scrape iniciado para um Scrape e não for um result parcial ao executar scrape para esse Scrape")
    fun shouldApplyNewScrapeResultOnScrapeAndChangeStatusToDoneWhenReceiveResultFromStartedScrapeForScrapeAndWasNotPartialResultOnExecutingScrapeForThatScrape() {
        val scrapedStringUrls: List<String> = listOf()
        val scrapedUrls: List<Url> = scrapedStringUrls.map { Url.of(url = it) }
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = scrapedStringUrls)
        val newScrapeResult = ScrapeResult.of(keyword = scrape.keyword, notPartialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(scrape.keyword))).thenReturn(newScrapeResult)

        scrapeExecutor.executeFor(scrape)

        verify(repository, atLeastOnce()).save(scrape = scrapeCaptor.capture())

        assertThat(scrapeCaptor.firstValue,allOf(
            instanceOf(Scrape::class.java),
            hasProperty("id",instanceOf<Id>(Id::class.java)),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("status", equalTo(ScrapeStatus.DONE)),
            hasProperty("result", allOf(
                instanceOf<ScrapeResult>(ScrapeResult::class.java),
                hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(instanceOf(Boolean::class.java), equalTo(notPartialResult))),
                hasProperty("scrapedUrls", allOf(instanceOf(List::class.java), equalTo(scrapedUrls)))
            ))))
    }

    @Test
    @DisplayName("Deve retornar ScrapeResult obtido a partir da scrape da Keyword iniciada quando executando scrape para um Scrape")
    fun shouldReturnScrapeResultObtainedFromStartedKeywordScrapingWhenExecutingScrapeForScrape() {
        val scrapedStringUrls: List<String> = listOf()
        val scrapedUrls: List<Url> = scrapedStringUrls.map { Url.of(url = it) }
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = scrapedStringUrls)
        val newScrapeResult = ScrapeResult.of(keyword = scrape.keyword, notPartialResult, scrapedUrls)
        `when`(webScraper.startKeywordScraping(keyword = eq(scrape.keyword))).thenReturn(newScrapeResult)

        val result = scrapeExecutor.executeFor(scrape)

        assertThat(result, allOf(
            instanceOf(ScrapeResult::class.java),
            equalTo(newScrapeResult)))
    }

}