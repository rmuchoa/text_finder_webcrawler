package com.crawl.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws")
data class AwsProperties(
    var region: String = "",
    var credentials: Credentials = Credentials(),
    var dynamodb: Dynamodb = Dynamodb(),
    var sqs: Sqs = Sqs()) {

    data class Credentials(
        var accessKey: String = "",
        var secretKey: String = "")

    data class Dynamodb(
        var endpoint: String = "")

    data class Sqs(
        var endpoint: String = "",
        var queueUrl: String = ""
    )
}

