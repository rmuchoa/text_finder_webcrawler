package com.crawl.infrastructure.client.jsoup

import com.crawl.domain.entity.WebPage
import com.crawl.domain.values.Url
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JsoupWebClient {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun getPageFromUrl(url: Url): WebPage? {

        return runCatching {
            Jsoup.connect(url.toString()).get()
        }.onSuccess { document ->
            log.debug("JSOUP CLIENT: Loaded web page content from url: {} html: {}", url, document.html())
        }.onFailure { exception ->
            log.error("JSOUP CLIENT: Some exception has occurred during web page loading from url: {}, {}", url, exception.message)
        }.getOrNull()?.let {
            JsoupWebPage(document = it)
        }

    }

}