package com.crawl.application.adapter.output

import com.crawl.application.port.output.VisitedUrlRepositoryPort
import com.crawl.application.service.WebPageFinder
import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.entity.WebPageDocument
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
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never

@ExtendWith(MockitoExtension::class)
class WebScraperTest : AbstractTest() {

    @Mock private lateinit var webPageFinder: WebPageFinder
    @Mock private lateinit var visitedUrlRepository: VisitedUrlRepositoryPort
    @Mock private lateinit var webPageDocument: WebPageDocument
    private lateinit var webScraper: WebScraper

    @BeforeEach
    fun setUp() {
        webScraper = WebScraper(webPageFinder, visitedUrlRepository)
    }

    @Test
    @DisplayName("Deve pedir ao VisitedUrlRepository para listar todas as urls visitadas quando iniciar scraping com a Keyword informada")
    fun shouldAskVisitedUrlRepositoryToListAllVisitedUrlsWhenStartingKeywordScrapingByKeyword() {
        val keyword = Keyword.of(defaultKeyword)

        webScraper.startKeywordScraping(keyword)

        verify(visitedUrlRepository, atLeastOnce()).listAllVisitedUrls()
    }

    @Test
    @DisplayName("Deve perguntar ao VisitedUrlRepository se ainda não finalizou a navegação nas páginas quando iniciar scraping com a Keyword informada")
    fun shouldAskVisitedUrlRepositoryIfDoesNotFinishedNavigationYetWhenStartingKeywordScrapingByKeyword() {
        val keyword = Keyword.of(defaultKeyword)

        webScraper.startKeywordScraping(keyword)

        verify(visitedUrlRepository, atLeastOnce()).doesNotFinishedNavigationYet()
    }

    @Test
    @DisplayName("Deve retornar CrawlResult baseado no estado atual de visitação encontrado no VisitedUrlRepository quando iniciar scraping com a Keyword informada")
    fun shouldReturnCrawlResultBasedOnVisitationCurrentStateFoundOnVisitedUrlRepositoryWhenStartKeywordScrapingByKeyword() {
        val keyword = Keyword.of(defaultKeyword)
        val emptyUrls: List<Url> = listOf()
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(emptyUrls)
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)

        val result = webScraper.startKeywordScraping(keyword)

        assertThat(result,allOf(
            instanceOf(ScrapeResult::class.java),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("partialResult", allOf(
                instanceOf(Boolean::class.java),
                equalTo(partialResult))),
            hasProperty("scrapedUrls", allOf(
                instanceOf(List::class.java),
                equalTo(emptyUrls)))
        ))
    }

    @Test
    @DisplayName("Deve jamais pedir ao WebPageFinder para encontrar página web a partir de url quando não tiver nenhuma url que não foi visitada ainda ao iniciar scraping com a Keyword informada")
    fun shouldNeverAskWebPageFinderToFindWebPageByUrlWhenHasNoOneUnvisitedUrlOnStartKeywordScrapingByKeyword() {
        val keyword = Keyword.of(defaultKeyword)
        val emptyUrls: List<Url> = listOf()
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(emptyUrls)
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)

        webScraper.startKeywordScraping(keyword)

        verify(webPageFinder, never()).findWebPageByUrl(url = any<Url>())
    }

    @Test
    @DisplayName("Deve pedir ao WebPageFinder para encontrar página web a partir de url quando tiver ao menos uma url que não foi visitada ainda ao iniciar scraping com a Keyword informada")
    fun shouldAskWebPageFinderToFindWebPageByUrlWhenHasAtLeastOnceUnvisitedUrlOnStartKeywordScrapingByKeyword() {
        val keyword = Keyword.of(defaultKeyword)
        val url = Url.of(url = myLink)
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(listOf(url))
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)

        webScraper.startKeywordScraping(keyword)

        verify(webPageFinder, atLeastOnce()).findWebPageByUrl(url = eq(url))
    }

    @Test
    @DisplayName("Deve perguntar à WebPage se ela contém a Keyword quando encontrar WebPage a partir de uma Url que não foi visitada ao realizar o scrape da Keyword enquanto iniciar scraping com a Keyword informada")
    fun shouldAskWebPageIfContainsKeywordWhenFindWebPageByUnvisitedUrlOnScrapeKeywordDuringStartKeywordScrapingByKeyword() {
        val url = Url.of(url = myLink)
        val keyword = Keyword.of(defaultKeyword)
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(listOf(url))
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)
        `when`(webPageFinder.findWebPageByUrl(url = eq(url))).thenReturn(webPageDocument)

        webScraper.startKeywordScraping(keyword)

        verify(webPageDocument, atLeastOnce()).scrapeKeyword(keyword = eq(keyword))
    }

    @Test
    @DisplayName("Deve retornar CrawlResult baseado em uma WebPage não encontrada por Url quando iniciar scraping com a Keyword informada")
    fun shouldReturnCrawlResultBasedOnNotFoundWebPageByUrlWhenStartKeywordScrapingByKeyword() {
        val url = Url.of(url = myLink)
        val keyword = Keyword.of(defaultKeyword)
        val nullWebPageDocument: WebPageDocument? = null
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(listOf(url))
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)
        `when`(webPageFinder.findWebPageByUrl(url = eq(url))).thenReturn(nullWebPageDocument)

        val result = webScraper.startKeywordScraping(keyword)

        assertThat(result,allOf(
            instanceOf(ScrapeResult::class.java),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("partialResult", allOf(
                instanceOf(Boolean::class.java),
                equalTo(partialResult))),
            hasProperty("scrapedUrls", allOf(
                instanceOf(List::class.java),
                equalTo(listOf<Url>())))
        ))
    }

    @Test
    @DisplayName("Deve retornar CrawlResult baseado em um Scrape falho realizado na WebPage encontrada por Url quando iniciar scraping com a Keyword informada")
    fun shouldReturnCrawlResultBasedOnFailedKeywordScrapeMadeOnWebPageFoundByUrlWhenStartKeywordScrapingByKeyword() {
        val url = Url.of(url = myLink)
        val keyword = Keyword.of(defaultKeyword)
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(listOf(url))
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(partialResult)
        `when`(webPageFinder.findWebPageByUrl(url = eq(url))).thenReturn(webPageDocument)
        `when`(webPageDocument.scrapeKeyword(keyword = eq(keyword))).thenReturn(false)

        val result = webScraper.startKeywordScraping(keyword)

        assertThat(result,allOf(
            instanceOf(ScrapeResult::class.java),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("partialResult", allOf(
                instanceOf(Boolean::class.java),
                equalTo(partialResult))),
            hasProperty("scrapedUrls", allOf(
                instanceOf(List::class.java),
                equalTo(listOf<Url>())))
        ))
    }

    @Test
    @DisplayName("Deve retornar CrawlResult baseado no estado de visitação finalizado e na WebPage encontrada por Url quando iniciar scraping com a Keyword informada")
    fun shouldReturnCrawlResultBasedOnVisitationFinishedStateAndWebPageFoundByUrlWhenStartKeywordScrapingByKeyword() {
        val url = Url.of(url = myLink)
        val keyword = Keyword.of(defaultKeyword)
        `when`(visitedUrlRepository.listAllVisitedUrls()).thenReturn(listOf(url))
        `when`(visitedUrlRepository.doesNotFinishedNavigationYet()).thenReturn(notPartialResult)
        `when`(webPageFinder.findWebPageByUrl(url = eq(url))).thenReturn(webPageDocument)
        `when`(webPageDocument.scrapeKeyword(keyword = eq(keyword))).thenReturn(true)

        val result = webScraper.startKeywordScraping(keyword)

        assertThat(result,allOf(
            instanceOf(ScrapeResult::class.java),
            hasProperty("keyword", instanceOf<Keyword>(Keyword::class.java)),
            hasProperty("partialResult", allOf(
                instanceOf(Boolean::class.java),
                equalTo(notPartialResult))),
            hasProperty("scrapedUrls", allOf(
                instanceOf(List::class.java),
                equalTo(listOf(url))))
        ))
    }

}