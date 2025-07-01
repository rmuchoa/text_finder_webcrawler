package com.crawl.infrastructure.notification.sqs

import com.crawl.infrastructure.notification.ScrapeNotificationReceiver
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SqsNotificationListener(
    val notificationReceiver: ScrapeNotificationReceiver) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${spring.cloud.aws.sqs.scrape-queue}")
    lateinit var queueName: String

    @SqsListener("\${spring.cloud.aws.sqs.scrape-queue}")
    fun listen(message: String) {

        log.debug("Received notification from queue: {} message: {}", queueName, message)
        notificationReceiver.receive(message)

    }

}