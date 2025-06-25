package com.crawl.infrastructure.client

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.WebPage
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url
import com.crawl.infrastructure.client.jsoup.JsoupWebClient
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
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class WebPageExternalClientAdapterTest : AbstractTest() {

    @Mock private lateinit var client: JsoupWebClient
    @Mock private lateinit var webPage: WebPage
    private lateinit var adapter: WebPageExternalClientAdapter

    @BeforeEach
    fun setUp() {
        adapter = WebPageExternalClientAdapter(client)
    }

    @Test
    @DisplayName("Deve pedir ao JsoupWebClient para buscar uma página para uma Url quando pegar um documento de página de uma Url")
    fun shouldAskJsoupWebClientToGetPageFromUrlWhenGettingPageDocumentFromUrl() {
        val url = Url.of(url = myLink)

        adapter.getPageDocumentFromUrl(url)

        verify(client, atLeastOnce()).getPageFromUrl(url = eq(url))
    }

    @Test
    @DisplayName("Deve retornar documento de página web nulo quando obter uma pagina web nula do JsoupWebClient ao pegar um documento de página de uma Url")
    fun shouldReturnNullWebPageDocumentWhenObtainNullWebPageFromJsoupWebClientOnGettingPageDocumentFromUrl() {
        val nullWebPage = null
        val url = Url.of(url = myLink)
        `when`(client.getPageFromUrl(url = eq(url))).thenReturn(nullWebPage)

        val result = adapter.getPageDocumentFromUrl(url)

        assertThat(result, equalTo(nullWebPage))
    }

    @Test
    @DisplayName("Deve retornar documento de página web quando obter uma pagina web a partir do JsoupWebClient ao pegar um documento de página de uma Url")
    fun shouldReturnWebPageDocumentWhenObtainSomeWebPageFromJsoupWebClientOnGettingPageDocumentFromUrl() {
        val url = Url.of(url = myLink)
        `when`(client.getPageFromUrl(url = eq(url))).thenReturn(webPage)
        `when`(webPage.hasHtmlElement()).thenReturn(true)
        `when`(webPage.hasBodyElement()).thenReturn(true)

        val result = adapter.getPageDocumentFromUrl(url)

        assertThat(result, allOf(
            instanceOf(WebPageDocument::class.java),
            hasProperty("pageUrl", allOf(
                instanceOf(Url::class.java),
                equalTo(url))),
            hasProperty("webPage", allOf(
                instanceOf(WebPage::class.java),
                equalTo(webPage)))))
    }

}