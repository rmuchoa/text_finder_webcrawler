package com.crawl.infrastructure.client

import com.crawl.application.port.output.WebPageExternalRepositoryPort
import com.crawl.domain.entity.WebPage
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url
import com.crawl.infrastructure.client.jsoup.JsoupWebClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WebPageExternalClientAdapter(
    val client: JsoupWebClient
) : WebPageExternalRepositoryPort {

    val log: Logger = LoggerFactory.getLogger(WebPageExternalClientAdapter::class.java)

    override fun getPageDocumentFromUrl(url: Url): WebPageDocument? {
        log.info("WEB PAGE CLIENT: Starting to load web page from url: {}", url)

        val webPage: WebPage? = client.getPageFromUrl(url)
        log.info("WEB PAGE CLIENT: Received web page from url: {}", url)

        return webPage?.let {
                val document: WebPageDocument = WebPageDocument.of(pageUrl = url, webPage = it)
                log.info("WEB PAGE CLIENT: Loaded web page! url: {}", url)
                document
            }
    }

}