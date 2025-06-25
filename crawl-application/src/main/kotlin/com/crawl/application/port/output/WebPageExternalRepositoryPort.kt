package com.crawl.application.port.output

import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url

interface WebPageExternalRepositoryPort {
    fun getPageDocumentFromUrl(url: Url): WebPageDocument?
}