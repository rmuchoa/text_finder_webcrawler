package com.crawl.domain.service

import com.crawl.domain.AbstractTest
import com.crawl.domain.entity.ScrapeIntention
import com.crawl.domain.port.output.ScrapeNotificationDeliverPort
import com.crawl.domain.port.output.ScrapeRepositoryPort
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
class ScrapeIntentorTest : AbstractTest() {

    @Mock private lateinit var repository: ScrapeRepositoryPort
    @Mock private lateinit var deliver: ScrapeNotificationDeliverPort
    private lateinit var scrapeIntentor: ScrapeIntentor

    @BeforeEach
    fun setUp() {
        scrapeIntentor = ScrapeIntentor(repository, deliver)
    }

    @Test
    @DisplayName("Deve pedir ao repositório para salvar uma ScrapeIntention quando intentar a partir de uma Intention informada")
    fun shouldAskRepositoryToSaveIntentionWhenIntentingANewIntention() {
        val intention: ScrapeIntention = ScrapeIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        scrapeIntentor.intent(intention)

        verify(repository, atLeastOnce()).save(eq(intention))
    }

    @Test
    @DisplayName("Deve pedir ao entregador para notificar um ScrapeId quando intentar uma ScrapeIntention informada")
    fun shouldAskDeliverToNotifyIntentedCrawIdWhenIntentingANewIntention() {
        val intention = ScrapeIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        scrapeIntentor.intent(intention)

        verify(deliver, atLeastOnce()).notifyIntentedScrape(eq(intention.getId()))
    }

    @Test
    @DisplayName("Deve retornar uma ScrapeIntention intentada quando intentar uma ScrapeIntention informada")
    fun shouldReturnIntentedScrapeWhenIntentingANewIntention() {
        val intention = ScrapeIntention.of(defaultKeyword)
        `when`(repository.save(eq(intention))).thenReturn(intention)

        val intentedScrape: ScrapeIntention? = scrapeIntentor.intent(intention)

        assertThat(intentedScrape, equalTo(intention))
    }

}