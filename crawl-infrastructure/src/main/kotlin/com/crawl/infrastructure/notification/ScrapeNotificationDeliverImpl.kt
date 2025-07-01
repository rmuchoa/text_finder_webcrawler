package com.crawl.infrastructure.notification

import com.crawl.application.notification.ScrapeNotification
import com.crawl.application.notification.ScrapeNotificationDeliver
import com.crawl.domain.values.Id
import com.crawl.infrastructure.notification.sqs.SqsAsyncNotificationBroker
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class ScrapeNotificationDeliverImpl(
    private val sqsAsyncNotificationBroker: SqsAsyncNotificationBroker):
    ScrapeNotificationDeliver {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${spring.cloud.aws.sqs.scrape-queue}")
    lateinit var queueName: String

    override fun notifyIntentedScrape(id: Id) {

        val notification = ScrapeNotificationDTO(id)

        log.info("Notifying intented scrape {} on {}", id, queueName)
        notifyScrapeMessage(notification, delaySeconds = 0)
    }

    override fun requeueUnfinishedScrape(notification: ScrapeNotification) {

        val retryNotification = notification.retry() as ScrapeNotificationDTO

        log.info("Notifying unfinished scrape {} on {} to restart scraping!", retryNotification.getId(), queueName)
        notifyScrapeMessage(notification = retryNotification, delaySeconds = 30)
    }

    private fun notifyScrapeMessage(notification: ScrapeNotificationDTO, delaySeconds: Int) {

        val jsonMessage = Json.encodeToString<ScrapeNotificationDTO>(value = notification)

        sqsAsyncNotificationBroker.notifyMessage(jsonMessage, queueName, delaySeconds)
        log.info("Notification sent for scrape {} on {}", notification.getId(), queueName)
    }

}