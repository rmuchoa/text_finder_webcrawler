package com.crawl.application.service

import com.crawl.application.port.output.WebPageExternalRepositoryPort
import com.crawl.application.port.output.WebPageInternalRepositoryPort
import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class WebPageFinderTest : AbstractTest() {

    @Mock private lateinit var externalRepository: WebPageExternalRepositoryPort
    @Mock private lateinit var internalRepository: WebPageInternalRepositoryPort
    @Mock private lateinit var webPageDocument: WebPageDocument
    private lateinit var webPageFinder: WebPageFinder

    @BeforeEach
    fun setUp() {
        webPageFinder = WebPageFinder(externalRepository, internalRepository)
    }

    @Test
    @DisplayName("Deve perguntar ao WebPageInternalRepository se a página web realmente existe no repositório interno da aplicação a partir da url quando procurar página web por url")
    fun shouldAskWebPageInternalRepositoryIfExistsWebPageByUrlWhenFindingWebPageByUrl() {
        val url = Url.of(url = myLink)

        webPageFinder.findWebPageByUrl(url)

        verify(internalRepository, atLeastOnce()).existsWebPageByUrl(url = eq(url))
    }

    @Test
    @DisplayName("Deve jamais pedir ao WebPageInternalRepository para encontrar página web por Url quando a página web não existir no repositório interno por Url enquanto procurando página web por url")
    fun shouldNeverAskWebPageInternalRepositoryToFindWebPageByUrlWhenDoesNotExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(false)

        webPageFinder.findWebPageByUrl(url)

        verify(internalRepository, never()).findWebPageByUrl(url = any<Url>())
    }

    @Test
    @DisplayName("Deve pedir ao WebPageInternalRepository para encontrar página web por Url quando notar que a página web existe no repositório interno para a Url informada enquanto procurando página web por url")
    fun shouldAskWebPageInternalRepositoryToFindWebPageByUrlWhenNoticeThatExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(true)

        webPageFinder.findWebPageByUrl(url)

        verify(internalRepository, atLeastOnce()).findWebPageByUrl(url = eq(url))
    }

    @Test
    @DisplayName("Deve retornar o WebPageDocument obtido a partir do repositório interno por Url quando notar que a página web existe no repositório interno para a Url informada enquanto procurando página web por url")
    fun shouldReturnObtainedWebPageDocumentFromWebPageInternalRepositoryWhenNoticeThatExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(true)
        `when`(internalRepository.findWebPageByUrl(url = eq(url))).thenReturn(webPageDocument)

        val result = webPageFinder.findWebPageByUrl(url)

        assertThat(result,allOf(
            instanceOf(WebPageDocument::class.java),
            equalTo(webPageDocument)))
    }

    @Test
    @DisplayName("Deve jamais pedir ao WebPageExternalRepository para pegar documento da página web por Url quando notar que a página web existe no repositório interno por Url enquanto procurando página web por url")
    fun shouldNeverAskWebPageExternalRepositoryToGetPageDocumentFromUrlWhenNoticeThatExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(true)

        webPageFinder.findWebPageByUrl(url)

        verify(externalRepository, never()).getPageDocumentFromUrl(url = any<Url>())
    }

    @Test
    @DisplayName("Deve pedir ao WebPageExternalRepository para pegar documento da página web por Url quando a página web não existir no repositório interno por Url enquanto procurando página web por url")
    fun shouldAskWebPageExternalRepositoryToGetPageDocumentFromUrlWhenDoesNotExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(false)

        webPageFinder.findWebPageByUrl(url)

        verify(externalRepository, atLeastOnce()).getPageDocumentFromUrl(url = eq(url))
    }

    @Test
    @DisplayName("Deve jamais pedir ao WebPageInternalRepository para salvar página web obtida no repositório externo por Url quando notar que a página web existe no repositório interno por Url enquanto procurando página web por url")
    fun shouldNeverAskWebPageInternalRepositoryToSaveObtainedWebPageOnExternalRepositoryWhenNoticeThatExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(true)

        webPageFinder.findWebPageByUrl(url)

        verify(internalRepository, never()).saveWebPage(webPage = any<WebPageDocument>())
    }

    @Test
    @DisplayName("Deve pedir ao WebPageInternalRepository para salvar página web obtida no repositório externo por Url quando a página web não existir no repositório interno por Url enquanto procurando página web por url")
    fun shouldAskWebPageInternalRepositoryToSaveObtainedWebPageOnExternalRepositoryWhenDoesNotExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(false)
        `when`(externalRepository.getPageDocumentFromUrl(url = eq(url))).thenReturn(webPageDocument)

        webPageFinder.findWebPageByUrl(url)

        verify(internalRepository, atLeastOnce()).saveWebPage(webPage = eq(webPageDocument))
    }

    @Test
    @DisplayName("Deve retornar o WebPageDocument obtido a partir do repositório externo por Url quando a página web não existir no repositório interno por Url enquanto procurando página web por url")
    fun shouldReturnObtainedWebPageDocumentFromWebPageExternalRepositoryWhenDoesNotExistsWebPageOnInternalRepositoryByGivenUrlOnFindingWebPageByUrl() {
        val url = Url.of(url = myLink)
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(false)
        `when`(externalRepository.getPageDocumentFromUrl(url = eq(url))).thenReturn(webPageDocument)

        val result = webPageFinder.findWebPageByUrl(url)

        assertThat(result,allOf(
            instanceOf(WebPageDocument::class.java),
            equalTo(webPageDocument)))
    }

    @Test
    @DisplayName("Deve tratar qualquer Exception que seja lançada pelo repositório interno quando procurar página web por url")
    fun shouldTreatAnyExceptionThatIsThrownByInternalRepositoryWhenFindWebPageByUrl() {
        val url = Url.of(url = myLink)
        val someExceptionMessage = "Some error occurrd!"
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(true)
        `when`(internalRepository.findWebPageByUrl(url = eq(url))).thenThrow(RuntimeException(someExceptionMessage))

        try {

            webPageFinder.findWebPageByUrl(url)

        } catch (throwable: Throwable) {
            fail(message = "Some error has occurred!", throwable)
        }
    }

    @Test
    @DisplayName("Deve tratar qualquer Exception que seja lançada pelo repositório externo quando procurar página web por url")
    fun shouldTreatAnyExceptionThatIsThrownByExternalRepositoryWhenFindWebPageByUrl() {
        val url = Url.of(url = myLink)
        val someExceptionMessage = "Some error occurrd!"
        `when`(internalRepository.existsWebPageByUrl(url = eq(url))).thenReturn(false)
        `when`(externalRepository.getPageDocumentFromUrl(url = eq(url))).thenThrow(RuntimeException(someExceptionMessage))

        try {

            webPageFinder.findWebPageByUrl(url)

        } catch (throwable: Throwable) {
            fail(message = "Some error has occurred!", throwable)
        }
    }

}