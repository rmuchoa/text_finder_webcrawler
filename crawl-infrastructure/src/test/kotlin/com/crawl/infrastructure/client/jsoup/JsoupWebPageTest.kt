package com.crawl.infrastructure.client.jsoup

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Keyword
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class JsoupWebPageTest : AbstractTest() {

    @Mock private lateinit var document: Document
    @Mock private lateinit var element: Element
    @Mock private lateinit var keyword: Keyword
    private lateinit var webPage: JsoupWebPage

    @BeforeEach
    fun setUp() {
        `when`(document.location()).thenReturn(myLink)
        `when`(document.html()).thenReturn(defaultKeywordPresentContent)
        webPage = JsoupWebPage(document)
    }

    @Test
    @DisplayName("Deve retornar falso quando o Document retornar um conteúdo html vazio")
    fun shouldReturnFalseWhenDocumentRetrieveEmptyHtmlContent() {
        `when`(document.html()).thenReturn(emptyStringContent)

        val result = webPage.hasHtmlElement()

        assertThat(result, equalTo(false))
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando o Document retornar um conteúdo html não vazio")
    fun shouldReturnTrueWhenDocumentRetrieveNonEmptyHtmlContent() {
        `when`(document.html()).thenReturn(defaultKeywordPresentContent)

        val result = webPage.hasHtmlElement()

        assertThat(result, equalTo(true))
    }

    @Test
    @DisplayName("Deve retornar falso quando o Document retornar um conteúdo body vazio")
    fun shouldReturnFalseWhenDocumentRetrieveEmptyBodyContent() {
        `when`(document.body()).thenReturn(element)
        `when`(element.html()).thenReturn(emptyStringContent)

        val result = webPage.hasBodyElement()

        assertThat(result, equalTo(false))
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando o Document retornar um conteúdo body não vazio")
    fun shouldReturnTrueWhenDocumentRetrieveNonEmptyBodyContent() {
        `when`(document.body()).thenReturn(element)
        `when`(element.html()).thenReturn(defaultKeywordPresentContent)

        val result = webPage.hasBodyElement()

        assertThat(result, equalTo(true))
    }

    @Test
    @DisplayName("Deve pedir ao Keyword para checar a própria presença no conteúdo da página dado enquanto checar se página web contém a Keyword")
    fun shouldAskKeywordToCheckPresenceOnGivenPageContentWhenCheckingWebPageContainsKeyword() {
        `when`(document.body()).thenReturn(element)
        `when`(element.text()).thenReturn(defaultKeywordPresentContent)

        webPage.containsKeyword(keyword)

        verify(keyword, atLeastOnce())
            .checkPresenceOn(pageContent = eq(defaultKeywordPresentContent))
    }

}