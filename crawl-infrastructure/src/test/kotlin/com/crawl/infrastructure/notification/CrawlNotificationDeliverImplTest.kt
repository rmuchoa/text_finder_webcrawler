package com.crawl.infrastructure.notification

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Id
import com.crawl.infrastructure.notification.sqs.SqsAsyncNotificationBroker
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class CrawlNotificationDeliverImplTest : AbstractTest() {

    @Mock private lateinit var sqsAsyncNotificationBroker: SqsAsyncNotificationBroker
    private val notificationCaptor = argumentCaptor<String>()
    private lateinit var notificationDeliver: CrawlNotificationDeliverImpl

    @BeforeEach
    fun setUp() {
        notificationDeliver = CrawlNotificationDeliverImpl(sqsAsyncNotificationBroker)
        notificationDeliver.queueName = QUEUE_NAME
    }

    @Test
    @DisplayName("Deve pedir ao broker assíncrono de notification sqs para notificar uma mensagem String contendo um objeto json com propriedade e valor de id e outros dados")
    fun shouldAskSqsAsyncNotificationBrokerNotifyStringMessageContainingJsonObjectContentWithIdAndOtherPropertyValues() {
        val id = Id.of(defaultId)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        notificationDeliver.notifyIntentedCrawl(id)

        verify(sqsAsyncNotificationBroker, atLeastOnce())
            .notifyMessage(
                message = notificationCaptor.capture(),
                queueName = eq(QUEUE_NAME),
                delaySeconds = eq(0))

        assertThat(notificationCaptor.firstValue,
            equalTo("""{"id":"$defaultId","retryCount":0,"firstAttempt":"$now","lastAttempt":"$now"}"""))
    }

    @Test
    @DisplayName("Deve pedir ao broker assíncrono de notification sqs para notificar uma mensagem String contendo um objeto json com uma propriedade id e seu valor")
    fun shouldAskSqsAsyncNotificationBrokerNotifyStringMessageContainingJsonObjectContentBasedOnRetryCrawlNotificationAndItsPropertiesAndValues() {
        val id = Id.of(defaultId)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val notification = CrawlNotificationDTO(id, retryCount = 1, firstAttempt = now)

        notificationDeliver.requeueUnfinishedCrawl(notification)

        verify(sqsAsyncNotificationBroker, atLeastOnce())
            .notifyMessage(
                message = notificationCaptor.capture(),
                queueName = eq(QUEUE_NAME),
                delaySeconds = eq(30))

        val isoWithoutMicrosecond = LocalDateTime.Format {
            date(LocalDate.Formats.ISO)
            char('T')
            hour(); char(':'); minute(); char(':'); second()
        }
        assertThat(notificationCaptor.firstValue,
            startsWith("""{"id":"$defaultId","retryCount":2,"firstAttempt":"$now","lastAttempt":"${isoWithoutMicrosecond.format(now)}"""))
    }

}