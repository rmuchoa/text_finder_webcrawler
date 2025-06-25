package com.crawl.application.service

import com.crawl.application.port.output.WebPageExternalRepositoryPort
import com.crawl.application.port.output.WebPageInternalRepositoryPort
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.exception.InvalidWebPageException
import com.crawl.domain.values.Url
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebPageFinder(
    val externalRepository: WebPageExternalRepositoryPort,
    val internalRepository: WebPageInternalRepositoryPort) {

    val log: Logger = LoggerFactory.getLogger(WebPageFinder::class.java)

    fun findWebPageByUrl(url: Url): WebPageDocument? {

        return runCatching {
            loadWebPage(url)
        }.onFailure { throwable ->
            treatInvalidWebPageException(throwable)
        }.getOrNull()
    }

    private fun loadWebPage(url: Url): WebPageDocument? {

        log.debug("WEB PAGE: Checking if web page already exists on file system structure! url: {}", url)
        if (internalRepository.existsWebPageByUrl(url)) return internalRepository.findWebPageByUrl(url)

        log.debug("WEB PAGE: This web page doesn't exist in the file system! url: {}", url)
        return externalRepository.getPageDocumentFromUrl(url)?.also {
            internalRepository.saveWebPage(webPage = it)
        }?: run {
            log.error("WEB PAGE: This web page doesn't exist on the web either! {}", url)
            null
        }
    }

    private fun treatInvalidWebPageException(throwable: Throwable) {
        throwable.takeIf { it is InvalidWebPageException }?.run {
            log.error("WEB PAGE: Some problem was notice on web page validation! {}", message);
        }
    }

}