package com.crawl.application.port.output

import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url

interface WebPageInternalRepositoryPort {
    fun findWebPageByUrl(url: Url): WebPageDocument?
    fun saveWebPage(webPage: WebPageDocument): WebPageDocument?
    fun existsWebPageByUrl(url: Url): Boolean
}