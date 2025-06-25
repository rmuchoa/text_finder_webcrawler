package com.crawl.infrastructure.repository.memory

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Url
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import com.crawl.infrastructure.repository.memory.VisitedUrlMemoryCacheRepository.VisitationStatus

@ExtendWith(MockitoExtension::class)
class VisitedUrlMemoryCacheRepositoryTest : AbstractTest() {

    private val repository = VisitedUrlMemoryCacheRepository()

    @Test
    fun shouldReturnAllCachedUrlOnSortedListWhenListingAllSortedUrls() {
        val firstUrl = Url.of("a")
        val secondUrl = Url.of("c")
        val thirdUrl = Url.of("d")
        val fourthUrl = Url.of("b")
        VisitedUrlMemoryCacheRepository.visitedUrlCache.add(firstUrl)
        VisitedUrlMemoryCacheRepository.visitedUrlCache.add(secondUrl)
        VisitedUrlMemoryCacheRepository.visitedUrlCache.add(thirdUrl)
        VisitedUrlMemoryCacheRepository.visitedUrlCache.add(fourthUrl)

        val result = repository.listAllSortedUrls()

        assertThat(result,equalTo(listOf(firstUrl, fourthUrl, secondUrl, thirdUrl)))
    }

    @Test
    fun shouldReturnFalseAlreadyFinishedNavigationFlagWhenVisitationStatusWasNeverChanged() {
        val result = repository.alreadyFinishedNavigation()

        assertThat(result,equalTo(false))
    }

    @Test
    fun shouldReturnFalseAlreadyFinishedNavigationFlagWhenVisitationStatusIsClearlyPending() {
        VisitedUrlMemoryCacheRepository.visitationStatus = VisitationStatus.PENDING

        val result = repository.alreadyFinishedNavigation()

        assertThat(result,equalTo(false))
    }

    @Test
    fun shouldReturnFalseAlreadyFinishedNavigationFlagWhenVisitationStatusWasChangedToStarted() {
        VisitedUrlMemoryCacheRepository.visitationStatus = VisitationStatus.STARTED

        val result = repository.alreadyFinishedNavigation()

        assertThat(result,equalTo(false))
    }

    @Test
    fun shouldReturnTrueAlreadyFinishedNavigationFlagWhenVisitationStatusWasChangedToFinished() {
        VisitedUrlMemoryCacheRepository.visitationStatus = VisitationStatus.FINISHED

        val result = repository.alreadyFinishedNavigation()

        assertThat(result,equalTo(true))
    }

}