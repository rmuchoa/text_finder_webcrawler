package com.crawl.infrastructure.notification.sqs

import com.crawl.domain.AbstractTest
import com.crawl.infrastructure.notification.CrawlNotificationReceiver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class SqsNotificationListenerTest : AbstractTest() {

    @Mock private lateinit var notificationReceiver: CrawlNotificationReceiver
    private lateinit var listener: SqsNotificationListener

    @BeforeEach
    fun setUp() {
        listener = SqsNotificationListener(notificationReceiver)
        listener.queueName = QUEUE_NAME
    }

    @Test
    @DisplayName("Deve pedir ao recebedor de notificações para receber uma mensagem quando escutar uma mensagem via SQS")
    fun shouldAskNotificationReceiverToReceiveListenedMessageWhenListeningSomeMessage() {
        val message = "{\"id\":\"$defaultId\"}"

        listener.listen(message)

        verify(notificationReceiver, atLeastOnce()).receive(eq(message))
    }

}