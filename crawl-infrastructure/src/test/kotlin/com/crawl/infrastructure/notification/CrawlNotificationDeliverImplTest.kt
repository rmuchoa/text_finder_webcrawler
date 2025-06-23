package com.crawl.infrastructure.notification

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.Id
import com.crawl.infrastructure.notification.sqs.SqsAsyncNotificationBroker
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
    @DisplayName("Deve pedir ao broker assíncrono de notification sqs para notificar uma mensagem String contendo um objeto json com uma propriedade id e seu valor")
    fun shouldAskSqsAsyncNotificationBrokerNotifyStringMessageContainingJsonObjectContentWithIdPropertyAndValue() {
        val id = Id.of(defaultId)

        notificationDeliver.notifyIntentedCrawl(id)

        verify(sqsAsyncNotificationBroker, atLeastOnce())
            .notifyMessage(
                message = notificationCaptor.capture(),
                queueName = eq(QUEUE_NAME))

        assertThat(notificationCaptor.firstValue,
            equalTo("{\"id\":\"${ defaultId }\"}"))
    }

}