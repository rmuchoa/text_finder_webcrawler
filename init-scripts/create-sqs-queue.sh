#!/bin/bash
# Crir filas SQS
awslocal sqs create-queue --queue-name CrawlNotificationQueue_DLQ
DLQ_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name CrawlNotificationQueue_DLQ --query "QueueUrl" --output text)
DLQ_QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url $DLQ_QUEUE_URL --attribute-names QueueArn --query 'Attributes.QueueArn' --output text)
awslocal sqs create-queue --queue-name CrawlNotificationQueue --attributes "{\"RedrivePolicy\":\"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_QUEUE_ARN\\\",\\\"maxReceiveCount\\\":\\\"5\\\"}\"}"
awslocal sqs create-queue --queue-name CrawlNotificationQueue_Retry --attributes DelaySeconds=30

echo "Setup concluído:"
echo "SQS Queue: CrawlNotificationQueue, CrawlNotificationQueue_DLQ e CrawlNotificationQueue_Retry"