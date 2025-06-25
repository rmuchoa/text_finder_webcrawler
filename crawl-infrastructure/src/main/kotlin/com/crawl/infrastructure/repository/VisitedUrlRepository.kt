package com.crawl.infrastructure.repository

import com.crawl.application.port.output.VisitedUrlRepositoryPort
import com.crawl.domain.values.Url
import com.crawl.infrastructure.repository.memory.VisitedUrlMemoryCacheRepository
import org.springframework.stereotype.Repository

@Repository
class VisitedUrlRepository(
    val visitedUrlMemoryCache: VisitedUrlMemoryCacheRepository
) : VisitedUrlRepositoryPort {

    override fun listAllVisitedUrls(): List<Url> {
        return visitedUrlMemoryCache.listAllSortedUrls()
    }

    override fun doesNotFinishedNavigationYet(): Boolean {
        return !visitedUrlMemoryCache.alreadyFinishedNavigation()
    }

}