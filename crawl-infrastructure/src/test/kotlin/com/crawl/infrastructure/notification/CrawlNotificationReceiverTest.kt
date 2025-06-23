package com.crawl.infrastructure.notification

import com.crawl.application.port.input.CrawlWaveReleaserPort
import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Id
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor

@ExtendWith(MockitoExtension::class)
class CrawlNotificationReceiverTest : AbstractTest() {

    @Mock private lateinit var waveProcessorPort: CrawlWaveReleaserPort
    private val idCaptor = argumentCaptor<Id>()
    private lateinit var notificationReceiver: CrawlNotificationReceiver

    @BeforeEach
    fun setUp() {
        notificationReceiver = CrawlNotificationReceiver(waveProcessorPort)
    }

    @Test
    @DisplayName("Deve converter a string json da mensagem em um CrawlNotification e liberar uma onda de processamento de Crawl através do Id do crawl recebido a partir da notificação")
    fun shouldConvertStringJsonMessageIntoCrawlNotificationAndReleaseCrawlProcessingWaveByCrawlIdReceivedFromCrawlNotification() {
        val message = "{\"id\":\"$defaultId\"}"

        notificationReceiver.receive(message)

        verify(waveProcessorPort, atLeastOnce())
            .releaseWave(crawlId = idCaptor.capture())

        assertThat(idCaptor.firstValue,equalTo(Id.of(defaultId)))
    }

}