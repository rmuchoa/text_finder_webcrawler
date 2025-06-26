package com.crawl.infrastructure.notification.sqs

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Repository
class SqsAsyncNotificationBroker(
    private val sqsAsyncClient: SqsAsyncClient) {

    var log: Logger = LoggerFactory.getLogger(SqsAsyncNotificationBroker::class.java)

    fun notifyMessage(message: String, queueName: String, delaySeconds: Int) {

        log.debug("Finding queue url for {} to start notifying", queueName)
        val queueUrl = sqsAsyncClient.getQueueUrl {
            it.queueName(queueName)
        }.get().queueUrl()

        log.debug("Notifying message on queue: {} message: {}", queueUrl, message)
        sqsAsyncClient.sendMessage {
            it.queueUrl(queueUrl)
                .messageBody(message)
                .delaySeconds(delaySeconds)
        }

        log.debug("Notification sent on queue: {} message: {}", queueUrl, message)
    }

}