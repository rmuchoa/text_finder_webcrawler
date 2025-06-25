package com.crawl.infrastructure.repository

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Url
import com.crawl.infrastructure.repository.memory.VisitedUrlMemoryCacheRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class VisitedUrlRepositoryTest : AbstractTest() {

    @Mock private lateinit var visitedUrlMemoryCache: VisitedUrlMemoryCacheRepository
    private lateinit var repository: VisitedUrlRepository

    @BeforeEach
    fun setUp() {
        repository = VisitedUrlRepository(visitedUrlMemoryCache)
    }

    @Test
    @DisplayName("Deve pedir ao VisitedUrlMemoryCache para listar todas as urls ordenadas quando listar todas as urls visitadas")
    fun shouldAskVisitedUrlMemoryCacheToListAllSortedUrlsWhenListingAllVisitedUrls() {
        repository.listAllVisitedUrls()

        verify(visitedUrlMemoryCache, atLeastOnce()).listAllSortedUrls()
    }

    @Test
    @DisplayName("Deve retornar lista de urls visitadas quando listar todas as urls visitadas")
    fun shouldReturnVisitedUrlListWhenListingAllVisitedUrls() {
        val urls = listOf(Url.of(url = myLink))
        `when`(visitedUrlMemoryCache.listAllSortedUrls()).thenReturn(urls)

        val result = repository.listAllVisitedUrls()

        assertThat(result,equalTo(urls))
    }

    @Test
    @DisplayName("Deve pedir ao VisitedUrlMemoryCache se já tiver finalizado a navegação quando estiver checando se não finalizou a navegação ainda")
    fun shouldAskVisitedUrlMemoryCacheIfAlreadyFinishedNavigationWhenDoesNotFinishedNavigationYet() {
        repository.doesNotFinishedNavigationYet()

        verify(visitedUrlMemoryCache, atLeastOnce()).alreadyFinishedNavigation()
    }

    @Test
    @DisplayName("Deve retornar falso quando constatar já ter finalizado a navegação enquanto estiver checando se não finalizou a navegação ainda")
    fun shouldReturnFalseWhenAlreadyFinishedNavigationOnCallingDoesNotFinishedNavigationYet() {
        `when`(visitedUrlMemoryCache.alreadyFinishedNavigation()).thenReturn(true)

        val result = repository.doesNotFinishedNavigationYet()

        assertThat(result,equalTo(false))
    }

    @Test
    @DisplayName("Deve retornar true quando constatar já não ter finalizado navegação enquanto checando se não finalizou navegação ainda")
    fun shouldReturnTrueWhenDoesNotAlreadyFinishedNavigationOnCallingDoesNotFinishedNavigationYet() {
        `when`(visitedUrlMemoryCache.alreadyFinishedNavigation()).thenReturn(false)

        val result = repository.doesNotFinishedNavigationYet()

        assertThat(result,equalTo(true))
    }

}