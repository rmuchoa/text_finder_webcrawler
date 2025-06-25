package com.crawl.domain.entity

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
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
class WebPageDocumentTest : AbstractTest() {

    @Mock private lateinit var webPage: WebPage

    @Test
    @DisplayName("Deve perguntar ao component WebPage se contém a Keyword enquanto faz o scrape da Keyword")
    fun shouldAskWebPageComponentIfContainsKeywordWhenScrapingKeyword() {
        val pageUrl = Url.of(url = myLink)
        val keyword = Keyword.of(defaultKeyword)
        `when`(webPage.hasHtmlElement()).thenReturn(true)
        `when`(webPage.hasBodyElement()).thenReturn(true)
        val webPageDocument = WebPageDocument.of(pageUrl, webPage)

        webPageDocument.scrapeKeyword(keyword)

        verify(webPage, atLeastOnce()).containsKeyword(keyword = eq(keyword))
    }

}