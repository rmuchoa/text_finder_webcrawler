package com.crawl.application.adapter.input

import com.crawl.application.notification.ScrapeNotification
import com.crawl.application.notification.ScrapeNotificationDeliver
import com.crawl.domain.AbstractTest
import com.crawl.domain.exception.UnfinishedWebScrapingException
import com.crawl.domain.service.ScrapeProcessor
import com.crawl.domain.values.Id
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class ScrapeWaveReleaserTest: AbstractTest() {

    @Mock private lateinit var scrapeProcessor: ScrapeProcessor
    @Mock private lateinit var notificationDeliver: ScrapeNotificationDeliver
    private lateinit var waveReleaser: ScrapeWaveReleaser
    private lateinit var notification: ScrapeNotification
    private lateinit var scrapeId: Id

    @BeforeEach
    fun setUp() {
        waveReleaser = ScrapeWaveReleaser(scrapeProcessor, notificationDeliver)

        scrapeId = Id.of(defaultId)
        notification = object : ScrapeNotification {
            override fun getId(): Id { return scrapeId }
            override fun retry(): ScrapeNotification {
                return this
            }
        }
    }

    @Test
    @DisplayName("Deve pedir ao ScrapeProcessor para carregar e processar um Scrape via ScrapeId quando liberar uma nova onda de processamento de Scrape")
    fun shouldAskScrapeProcessorToLoadAndProcessScrapeByScrapeIdWhenReleaseNewProcessingWaveOnScrapeNotification() {

        waveReleaser.releaseWave(notification)

        verify(scrapeProcessor, atLeastOnce()).loadAndProcess(scrapeId = eq(scrapeId))
    }

    @Test
    @DisplayName("Deve nunca pedir ao ScrapeNotificationDeliver para reenfileirar Scrape quando liberar onda que complete o processamento inteiro para Scrape")
    fun shouldNeverAskNotificationDeliverToRequeueUnfinishedScrapeWhenFullyReleaseProcessingWaveForScrape() {

        waveReleaser.releaseWave(notification)

        verify(notificationDeliver, never()).requeueUnfinishedScrape(notification = any<ScrapeNotification>())
    }

    @Test
    @DisplayName("Deve pedir ao ScrapeNotificationDeliver para reenfileirar Scrape não finalizado quando capturar uma UnfinishedWebScrapeingException ao liberar onda de processamento para Scrape")
    fun shouldAskNotificationDeliverToRequeueUnfinishedScrapeWhenCatchUnfinishedWebScrapeingExceptionOnReleasingProcessingWaveForScrape() {
        `when`(scrapeProcessor.loadAndProcess(scrapeId = eq(scrapeId)))
            .thenThrow(UnfinishedWebScrapingException("Scrape não finalizou completamente"))

        waveReleaser.releaseWave(notification)

        verify(notificationDeliver, atLeastOnce()).requeueUnfinishedScrape(notification = eq(notification))
    }

}