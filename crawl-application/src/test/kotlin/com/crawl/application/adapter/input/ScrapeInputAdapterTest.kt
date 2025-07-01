package com.crawl.application.adapter.input

import com.crawl.application.dto.ScrapeRequest
import com.crawl.application.dto.RequestedScrape
import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.service.ScrapeIntentor
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasLength
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.matchesPattern
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ScrapeInputAdapterTest: AbstractTest() {

    @Mock private lateinit var scrapeIntentor: ScrapeIntentor
    private val scrapeIntentionCaptor = argumentCaptor<ScrapeIntention>()
    private lateinit var adapter: ScrapeInputAdapter

    @BeforeEach
    fun setUp() {
        adapter = ScrapeInputAdapter(scrapeIntentor)
    }

    @Test
    @DisplayName("Deve criar uma ScrapeIntention com base no ScrapeRequest informado e intentar uma intention através de um ScrapeIntentor")
    fun shouldCreateScrapeIntentionAndExecuteIntentionPassingKeywordAndGeneratingRandomId() {
        val keyword = "teste"
        val request = ScrapeRequest(keyword)
        val intention = ScrapeIntention.of(keyword)
        `when`(scrapeIntentor.intent(any<ScrapeIntention>()))
            .thenReturn(intention)

        adapter.requestScrape(request)

        verify(scrapeIntentor, atLeastOnce()).intent(scrapeIntentionCaptor.capture())
        assertThat(scrapeIntentionCaptor.firstValue,allOf(
            instanceOf(ScrapeIntention::class.java),
            hasProperty("id",allOf<Any?>(
                instanceOf(Id::class.java),
                hasProperty("id",allOf(
                    instanceOf<Any?>(String::class.java),
                    matchesPattern("[a-zA-Z0-9]+"),
                    hasLength(8))))),
            hasProperty("keyword",allOf<Any?>(
                instanceOf(Keyword::class.java),
                hasProperty("keyword",allOf(
                    instanceOf(String::class.java),
                    equalTo(keyword)))))))
    }

    @Test
    @DisplayName("Deve criar um ScrapeIntention com base no ScrapeRequest informado e retornar um RequestedScrape contendo o id gerado randomicamente")
    fun shouldCreateScrapeIntentionAndReturningRandomId() {
        val keyword = "teste"
        val request = ScrapeRequest(keyword)
        val intention = ScrapeIntention.of(keyword)
        `when`(scrapeIntentor.intent(any<ScrapeIntention>()))
            .thenReturn(intention)

        val requested: RequestedScrape? = adapter.requestScrape(request)

        assertThat(requested,
            hasProperty("id", instanceOf<Any?>(String::class.java)))
    }

}