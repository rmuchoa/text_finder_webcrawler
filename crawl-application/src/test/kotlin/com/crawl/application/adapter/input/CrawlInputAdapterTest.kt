package com.crawl.application.adapter.input

import com.crawl.application.dto.CrawlRequest
import com.crawl.application.dto.RequestedCrawl
import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.service.CrawlIntentor
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
class CrawlInputAdapterTest: AbstractTest() {

    @Mock private lateinit var crawlIntentor: CrawlIntentor
    private val crawlIntentionCaptor = argumentCaptor<CrawlIntention>()
    private lateinit var adapter: CrawlInputAdapter

    @BeforeEach
    fun setUp() {
        adapter = CrawlInputAdapter(crawlIntentor)
    }

    @Test
    fun shouldCreateCrawlIntentionAndExecuteIntentionPassingKeywordAndGeneratingRandomId() {
        val keyword = "teste"
        val request = CrawlRequest(keyword)
        val intention = CrawlIntention.of(keyword)
        `when`(crawlIntentor.intent(any<CrawlIntention>()))
            .thenReturn(intention)

        adapter.requestCrawl(request)

        verify(crawlIntentor, atLeastOnce()).intent(crawlIntentionCaptor.capture())
        assertThat(crawlIntentionCaptor.firstValue,allOf(
            instanceOf(CrawlIntention::class.java),
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
    fun shouldCreateCrawlIntentionAndReturningRandomId() {
        val keyword = "teste"
        val request = CrawlRequest(keyword)
        val intention = CrawlIntention.of(keyword)
        `when`(crawlIntentor.intent(any<CrawlIntention>()))
            .thenReturn(intention)

        val requested: RequestedCrawl? = adapter.requestCrawl(request)

        assertThat(requested,
            hasProperty("id", instanceOf<Any?>(String::class.java)))
    }

}