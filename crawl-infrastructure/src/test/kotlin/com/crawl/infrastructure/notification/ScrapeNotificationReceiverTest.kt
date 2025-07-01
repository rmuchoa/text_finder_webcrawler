package com.crawl.infrastructure.notification

import com.crawl.application.notification.ScrapeNotification
import com.crawl.application.port.input.ScrapeWaveReleaserPort
import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
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
class ScrapeNotificationReceiverTest : AbstractTest() {

    @Mock private lateinit var waveProcessorPort: ScrapeWaveReleaserPort
    private val notificationCaptor = argumentCaptor<ScrapeNotification>()
    private lateinit var notificationReceiver: ScrapeNotificationReceiver

    @BeforeEach
    fun setUp() {
        notificationReceiver = ScrapeNotificationReceiver(waveProcessorPort)
    }

    @Test
    @DisplayName("Deve converter a string json da mensagem em um ScrapeNotification e liberar uma onda de processamento de Scrape através do Id do Scrape recebido a partir da notificação")
    fun shouldConvertStringJsonMessageIntoScrapeNotificationAndReleaseScrapeProcessingWaveByScrapeIdReceivedFromScrapeNotification() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val message = """
            {
                "id":"$defaultId",
                "retryCount": 0,
                "firstAttempt": "$now",
                "lastAttempt": "$now"
            }
        """.trimIndent()

        notificationReceiver.receive(message)

        verify(waveProcessorPort, atLeastOnce())
            .releaseWave(notification = notificationCaptor.capture())

        assertThat(notificationCaptor.firstValue,allOf(
            instanceOf(ScrapeNotificationDTO::class.java),
            hasProperty("id", allOf(
                instanceOf(Id::class.java), equalTo(Id.of(defaultId)))),
            hasProperty("retryCount", allOf(
                instanceOf(Int::class.java), equalTo(0))),
            hasProperty("firstAttempt", allOf(
                instanceOf(LocalDateTime::class.java), equalTo(now))),
            hasProperty("lastAttempt", allOf(
                instanceOf(LocalDateTime::class.java), equalTo(now)))
        ))
    }

}