package com.crawl.infrastructure.client.jsoup

import com.crawl.domain.entity.WebPage
import com.crawl.domain.values.Keyword
import org.jsoup.nodes.Document

data class JsoupWebPage(val document: Document) : WebPage {

    val location: String = document.location()
    val html: String = document.html()

    override fun hasHtmlElement(): Boolean = document.html().isNotBlank()
    override fun hasBodyElement(): Boolean = document.body().html().isNotBlank()
    override fun containsKeyword(keyword: Keyword): Boolean =
        document.body().text().let {
            keyword.checkPresenceOn(pageContent = it)
        }

}
