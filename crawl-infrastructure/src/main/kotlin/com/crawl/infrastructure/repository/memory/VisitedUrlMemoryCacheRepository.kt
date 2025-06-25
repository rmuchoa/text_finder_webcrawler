package com.crawl.infrastructure.repository.memory

import com.crawl.domain.values.Url
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class VisitedUrlMemoryCacheRepository {

    fun listAllSortedUrls(): List<Url> {
        return visitedUrlCache
            .sortedBy { it.url.lowercase() }
            .toList()
    }

    fun alreadyFinishedNavigation(): Boolean {
        return visitationStatus == VisitationStatus.FINISHED
    }

    companion object {
        var visitationStatus: VisitationStatus = VisitationStatus.PENDING
        val visitedUrlCache: MutableSet<Url> = ConcurrentHashMap.newKeySet()
    }

    enum class VisitationStatus {
        PENDING,
        STARTED,
        FINISHED
    }

}