package com.crawl.application.adapter.input

import com.crawl.application.notification.CrawlNotificationDeliver
import com.crawl.domain.AbstractTest
import com.crawl.domain.exception.UnfinishedWebCrawlingException
import com.crawl.domain.service.CrawlProcessor
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
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class CrawlWaveReleaserTest: AbstractTest() {

    @Mock private lateinit var crawlProcessor: CrawlProcessor
    @Mock private lateinit var notificationDeliver: CrawlNotificationDeliver
    private lateinit var waveReleaser: CrawlWaveReleaser

    @BeforeEach
    fun setUp() {
        waveReleaser = CrawlWaveReleaser(crawlProcessor, notificationDeliver)
    }

    @Test
    @DisplayName("Deve pedir ao CrawlProcessor para carregar e processar um Crawl via CrawlId quando liberar uma nova onda de processamento de Crawl")
    fun shouldAskCrawlProcessorToLoadAndProcessCrawlByCrawlIdWhenReleaseNewProcessingWaveOnCrawl() {
        val crawlId = Id.of(defaultId)

        waveReleaser.releaseWave(crawlId)

        verify(crawlProcessor, atLeastOnce()).loadAndProcess(crawlId = eq(crawlId))
    }

    @Test
    @DisplayName("Deve nunca pedir ao CrawlNotificationDeliver para reenfileirar Crawl quando liberar onda que complete o processamento inteiro para Crawl")
    fun shouldNeverAskNotificationDeliverToRequeueUnfinishedCrawlWhenFullyReleaseProcessingWaveForCrawl() {
        val crawlId = Id.of(defaultId)

        waveReleaser.releaseWave(crawlId)

        verify(notificationDeliver, never()).requeueUnfinishedCrawl(id = eq(crawlId))
    }

    @Test
    @DisplayName("Deve pedir ao CrawlNotificationDeliver para reenfileirar Crawl não finalizado quando capturar uma UnfinishedWebCrawlingException ao liberar onda de processamento para Crawl")
    fun shouldAskNotificationDeliverToRequeueUnfinishedCrawlWhenCatchUnfinishedWebCrawlingExceptionOnReleasingProcessingWaveForCrawl() {
        val crawlId = Id.of(defaultId)
        `when`(crawlProcessor.loadAndProcess(crawlId = eq(crawlId)))
            .thenThrow(UnfinishedWebCrawlingException("Crawl não finalizou completamente"))

        waveReleaser.releaseWave(crawlId)

        verify(notificationDeliver, atLeastOnce()).requeueUnfinishedCrawl(id = eq(crawlId))
    }

}