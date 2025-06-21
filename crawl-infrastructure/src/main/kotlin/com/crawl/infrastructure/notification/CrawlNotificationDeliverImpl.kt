package com.crawl.infrastructure.notification

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

        val notification = CrawlNotification(id)
        val jsonMessage = Json.encodeToString<CrawlNotification>(value = notification)

        log.info("Notifying intented crawl {} on {}", id, queueName)
        sqsAsyncNotificationBroker.notifyMessage(jsonMessage, queueName)

        log.info("Notification sent for crawl {} on {}", id, queueName)
    }

    override fun requeueUnfinishedCrawl(id: Id) {

        log.info("Requeuing unfinished crawl {} to restart scraping!", id)
        notifyIntentedCrawl(id)
    }

}