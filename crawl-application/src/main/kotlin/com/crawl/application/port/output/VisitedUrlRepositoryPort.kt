package com.crawl.application.port.output

import com.crawl.domain.values.Url

interface VisitedUrlRepositoryPort {
    fun listAllVisitedUrls(): List<Url>
    fun doesNotFinishedNavigationYet(): Boolean
}