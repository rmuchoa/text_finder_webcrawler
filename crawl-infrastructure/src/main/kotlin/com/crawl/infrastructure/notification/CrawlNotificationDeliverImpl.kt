package com.crawl.infrastructure.notification

import com.crawl.application.notification.CrawlNotification
import com.crawl.application.notification.CrawlNotificationDeliver
import com.crawl.domain.values.Id
import com.crawl.infrastructure.notification.sqs.SqsAsyncNotificationBroker
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class CrawlNotificationDeliverImpl(
    private val sqsAsyncNotificationBroker: SqsAsyncNotificationBroker):
    CrawlNotificationDeliver {

    var log: Logger = LoggerFactory.getLogger(CrawlNotificationDeliverImpl::class.java)

    @Value("\${spring.cloud.aws.sqs.crawl-queue}")
    lateinit var queueName: String

    override fun notifyIntentedCrawl(id: Id) {

        val notification = CrawlNotificationDTO(id)

        log.info("Notifying intented crawl {} on {}", id, queueName)
        notifyCrawlMessage(notification, delaySeconds = 0)
    }

    override fun requeueUnfinishedCrawl(notification: CrawlNotification) {

        val retryNotification = notification.retry() as CrawlNotificationDTO

        log.info("Notifying unfinished crawl {} on {} to restart scraping!", retryNotification.getId(), queueName)
        notifyCrawlMessage(notification = retryNotification, delaySeconds = 30)
    }

    private fun notifyCrawlMessage(notification: CrawlNotificationDTO, delaySeconds: Int) {

        val jsonMessage = Json.encodeToString<CrawlNotificationDTO>(value = notification)

        sqsAsyncNotificationBroker.notifyMessage(jsonMessage, queueName, delaySeconds)
        log.info("Notification sent for crawl {} on {}", notification.getId(), queueName)
    }

}