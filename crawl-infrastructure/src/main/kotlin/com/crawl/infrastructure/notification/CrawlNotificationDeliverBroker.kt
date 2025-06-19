package com.crawl.infrastructure.notification

import com.crawl.domain.port.output.CrawlNotificationDeliverPort
import com.crawl.domain.service.CrawlIntentor
import com.crawl.domain.values.Id
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Repository
class CrawlNotificationDeliverBroker(private val sqsClient: SqsClient): CrawlNotificationDeliverPort {

    @Value("\${aws.sqs.queue-url}")
    lateinit var testQueueUrl: String

    @Value("\${aws.sqs.endpoint}")
    lateinit var testSqsEndpoint: String

    var log: Logger = LoggerFactory.getLogger(CrawlNotificationDeliverBroker::class.java)

    override fun notifyIntentedCrawl(id: Id) {

        val notification = CrawlNotification(id)
        val jsonMessage = Json.encodeToString<CrawlNotification>(notification)

        log.info("Getting queueUrl {} on endpoint: {}", testQueueUrl, testSqsEndpoint)

        val queueUrl = sqsClient.getQueueUrl { it.queueName("CrawlNotificationQueue") }.queueUrl()
        log.info("Notifying intented crawl {} on queue: {}", id, queueUrl)

        val messageRequest = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(jsonMessage)
            .build()

        sqsClient.sendMessage(messageRequest)
    }

}