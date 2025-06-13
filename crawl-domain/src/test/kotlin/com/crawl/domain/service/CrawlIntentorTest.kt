package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.port.output.CrawlRepositoryPort
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CrawlIntentorTest : AbstractTest() {

    @Mock private lateinit var repository: CrawlRepositoryPort
    @Mock private lateinit var deliver: CrawlNotificationDeliverPort
    private lateinit var crawlIntentor: CrawlIntentor

    @BeforeEach
    fun setUp() {
        crawlIntentor = CrawlIntentor(repository, deliver)
    }

    @Test
    @DisplayName("Deve pedir ao repositório para salvar a CrawlIntention quando intentar a partir de uma Intention informada")
    fun shouldAskRepositoryToSaveIntentionWhenIntentingANewIntention() {
        val intention: CrawlIntention = CrawlIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        crawlIntentor.intent(intention)

        verify(repository, atLeastOnce()).save(eq(intention))
    }

    @Test
    @DisplayName("Deve pedir ao entregador para notificar um CrawlId quando intentar uma CrawlIntention informada")
    fun shouldAskDeliverToNotifyIntentedCrawIdWhenIntentingANewIntention() {
        val intention = CrawlIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        crawlIntentor.intent(intention)

        verify(deliver, atLeastOnce()).notifyIntentedCrawl(eq(intention.getId()))
    }

    @Test
    @DisplayName("Deve retornar uma CrawlIntention intentada quando intentar uma CrawlIntention informada")
    fun shouldReturnIntentedCrawlWhenIntentingANewIntention() {
        val intention = CrawlIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        val intentedCrawl: CrawlIntention? = crawlIntentor.intent(intention)

        assertThat(intentedCrawl, equalTo(intention))
    }

}