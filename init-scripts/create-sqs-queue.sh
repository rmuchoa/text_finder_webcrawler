#!/bin/bash
# Crir filas SQS
awslocal sqs create-queue --queue-name CrawlNotificationQueue_DLQ
DLQ_CRAWL_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name CrawlNotificationQueue_DLQ --query "QueueUrl" --output text)
DLQ_CRAWL_QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url $DLQ_CRAWL_QUEUE_URL --attribute-names QueueArn --query 'Attributes.QueueArn' --output text)
awslocal sqs create-queue --queue-name CrawlNotificationQueue --attributes "{\"RedrivePolicy\":\"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_CRAWL_QUEUE_ARN\\\",\\\"maxReceiveCount\\\":\\\"5\\\"}\"}"

awslocal sqs create-queue --queue-name ScrapeNotificationQueue_DLQ
DLQ_SCRAPE_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name ScrapeNotificationQueue_DLQ --query "QueueUrl" --output text)
DLQ_SCRAPE_QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url $DLQ_SCRAPE_QUEUE_URL --attribute-names QueueArn --query 'Attributes.QueueArn' --output text)
awslocal sqs create-queue --queue-name ScrapeNotificationQueue --attributes "{\"RedrivePolicy\":\"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_SCRAPE_QUEUE_ARN\\\",\\\"maxReceiveCount\\\":\\\"5\\\"}\"}"

echo "Setup concluído:"
echo "SQS Queues: CrawlNotificationQueue, CrawlNotificationQueue_DLQ, ScrapeNotificationQueue, ScrapeNotificationQueue_DLQ"