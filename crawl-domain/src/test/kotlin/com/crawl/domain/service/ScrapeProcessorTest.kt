package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Scrape
import com.crawl.domain.exception.UnfinishedWebScrapingException
import com.crawl.domain.port.output.ScrapeRepositoryPort
import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Id
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class ScrapeProcessorTest : AbstractTest() {

    @Mock private lateinit var repository: ScrapeRepositoryPort
    @Mock private lateinit var scrapeExecutor: ScrapeExecutor
    private lateinit var scrapeProcessor: ScrapeProcessor

    @BeforeEach
    fun setUp() {
        scrapeProcessor = ScrapeProcessor(repository, scrapeExecutor)
    }

    @Test
    @DisplayName("Deve pedir para o repositório carregar Scrape pelo id quando carregar e processar ScrapeId")
    fun shouldAskRepositoryToLoadScrapeByIdWhenLoadAndProcessScrapeId() {
        val scrapeId = Id.of(defaultId)
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, notPartialResult, scrapedUrls = listOf())
        `when`(repository.load(scrapeId = eq(scrapeId))).thenReturn(scrape)
        `when`(scrapeExecutor.executeFor(scrape = eq(scrape))).thenReturn(scrape.result)

        scrapeProcessor.loadAndProcess(scrapeId)

        verify(repository, atLeastOnce()).load(scrapeId = eq(scrapeId))
    }

    @Test
    @DisplayName("Deve pedir ao ScrapeScrapeExecutor para executar scrape para o Scrape encontrado quando carregar e processar ScrapeId")
    fun shouldAskScrapeScrapeExecutorToExecuteScrapeForFoundScrapeWhenLoadAndProcessScrapeId() {
        val scrapeId = Id.of(defaultId)
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, notPartialResult, scrapedUrls = listOf())
        `when`(repository.load(scrapeId = eq(scrapeId))).thenReturn(scrape)
        `when`(scrapeExecutor.executeFor(scrape = eq(scrape))).thenReturn(scrape.result)

        scrapeProcessor.loadAndProcess(scrapeId)

        verify(scrapeExecutor, atLeastOnce()).executeFor(scrape = eq(scrape))
    }

    @Test
    @DisplayName("Deve lançar UnfinishedWebScrapeingException quando receber da execução de scrape um ScrapeResult que tenha resultado parcial")
    fun shouldThrowUnfinishedWebScrapingExceptionWhenReceiveFromExecuteScrapeForScrapeResultThatHasPartialResult() {
        val scrapeId = Id.of(defaultId)
        val scrape = Scrape.of(defaultId, status = ScrapeStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = listOf())
        `when`(repository.load(scrapeId = eq(scrapeId))).thenReturn(scrape)
        `when`(scrapeExecutor.executeFor(scrape = eq(scrape))).thenReturn(scrape.result)

        val thrown: UnfinishedWebScrapingException? = assertThrows(
            UnfinishedWebScrapingException::class.java) {
            scrapeProcessor.loadAndProcess(scrapeId)
        }

        assertThat(thrown,
            hasProperty("message",
                equalTo("WebScraper doesn't finished page navigation yet to reach a full result")))

    }

}