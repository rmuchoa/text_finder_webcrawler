package com.crawl.infrastructure.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
@EnableConfigurationProperties(AwsProperties::class)
open class AwsSqsClientConfig {

    var log: Logger = LoggerFactory.getLogger(AwsSqsClientConfig::class.java)

    @Bean
    open fun sqsClient(
        properties: AwsProperties): SqsClient {

        log.info("AwsProperties used on SqsClient building: {}", properties)

        return SqsClient.builder()
            .region(Region.of(properties.region))
            .endpointOverride(URI.create(properties.sqs.endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        properties.credentials.accessKey,
                        properties.credentials.secretKey)))
            .build()
    }

}