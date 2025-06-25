package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.Crawl
import com.crawl.domain.exception.UnfinishedWebCrawlingException
import com.crawl.domain.port.output.CrawlRepositoryPort
import com.crawl.domain.values.CrawlStatus
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
class CrawlProcessorTest : AbstractTest() {

    @Mock private lateinit var repository: CrawlRepositoryPort
    @Mock private lateinit var crawlScrapeExecutor: CrawlScrapeExecutor
    private lateinit var crawlProcessor: CrawlProcessor

    @BeforeEach
    fun setUp() {
        crawlProcessor = CrawlProcessor(repository, crawlScrapeExecutor)
    }

    @Test
    @DisplayName("Deve pedir para o repositório carregar Crawl pelo id quando carregar e processar CrawlId")
    fun shouldAskRepositoryToLoadCrawlByIdWhenLoadAndProcessCrawlId() {
        val crawlId = Id.of(defaultId)
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, notPartialResult, scrapedUrls = listOf())
        `when`(repository.load(crawlId = eq(crawlId))).thenReturn(crawl)
        `when`(crawlScrapeExecutor.executeScrapeFor(crawl = eq(crawl))).thenReturn(crawl.result)

        crawlProcessor.loadAndProcess(crawlId)

        verify(repository, atLeastOnce()).load(crawlId = eq(crawlId))
    }

    @Test
    @DisplayName("Deve pedir ao CrawlScrapeExecutor para executar scrape para o Crawl encontrado quando carregar e processar CrawlId")
    fun shouldAskCrawlScrapeExecutorToExecuteScrapeForFoundCrawlWhenLoadAndProcessCrawlId() {
        val crawlId = Id.of(defaultId)
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, notPartialResult, scrapedUrls = listOf())
        `when`(repository.load(crawlId = eq(crawlId))).thenReturn(crawl)
        `when`(crawlScrapeExecutor.executeScrapeFor(crawl = eq(crawl))).thenReturn(crawl.result)

        crawlProcessor.loadAndProcess(crawlId)

        verify(crawlScrapeExecutor, atLeastOnce()).executeScrapeFor(crawl = eq(crawl))
    }

    @Test
    @DisplayName("Deve lançar UnfinishedWebCrawlingException quando receber da execução de scrape um CrawlResult que tenha resultado parcial")
    fun shouldThrowUnfinishedWebCrawlingExceptionWhenReceiveFromExecuteScrapeForCrawlResultThatHasPartialResult() {
        val crawlId = Id.of(defaultId)
        val crawl = Crawl.of(defaultId, status = CrawlStatus.ACTIVE.status, defaultKeyword, partialResult, scrapedUrls = listOf())
        `when`(repository.load(crawlId = eq(crawlId))).thenReturn(crawl)
        `when`(crawlScrapeExecutor.executeScrapeFor(crawl = eq(crawl))).thenReturn(crawl.result)

        val thrown: UnfinishedWebCrawlingException? = assertThrows(
            UnfinishedWebCrawlingException::class.java) {
            crawlProcessor.loadAndProcess(crawlId)
        }

        assertThat(thrown,
            hasProperty("message",
                equalTo("WebCrawler doesn't finished page navigation yet to reach a full result")))

    }

}