package com.crawl.infrastructure.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
@EnableConfigurationProperties(DynamoDBProperties::class)
open class DynamoDBConfig {

    @Bean
    open fun dynamoDbClient(
        properties: DynamoDBProperties): DynamoDbClient {

        return DynamoDbClient.builder()
            .region(Region.of("us-east-1"))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    properties.credentials.accessKey,
                    properties.credentials.secretKey)))
            .endpointOverride(URI.create(properties.endpoint))
            .build()
    }

    @Bean
    open fun dynamoDbEnhancedClient(
        @Qualifier("dynamoDbClient")
        dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {

        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

}