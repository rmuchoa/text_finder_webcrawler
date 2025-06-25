package com.crawl.domain.entity

import com.crawl.domain.exception.InvalidWebPageException
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url

@ConsistentCopyVisibility
data class WebPageDocument internal constructor(
    val pageUrl: Url,
    val webPage: WebPage) {

    fun scrapeKeyword(keyword: Keyword): Boolean = webPage.containsKeyword(keyword)

    companion object {
        fun of(pageUrl: Url, webPage: WebPage) :
                WebPageDocument = WebPageDocument(pageUrl, webPage = validWebPage(webPage))

        private fun validWebPage(webPage: WebPage) =
            withBodyElement(webPage = withHtmlElement(webPage))

        private fun withHtmlElement(webPage: WebPage): WebPage =
            webPage.takeIf { it.hasHtmlElement() }?: throw InvalidWebPageException("Invalid web page content without <html>...</html> element!")

        private fun withBodyElement(webPage: WebPage): WebPage =
            webPage.takeIf { it.hasBodyElement() }?: throw InvalidWebPageException("Invalid web page content without <body>...</body> element!")
    }

}
