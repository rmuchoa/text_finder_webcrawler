package com.crawl.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.dynamodb")
data class DynamoDBProperties(
    var endpoint: String = "",
    var credentials: Credentials = Credentials()) {

    data class Credentials(
        var accessKey: String = "",
        var secretKey: String = "")
}

