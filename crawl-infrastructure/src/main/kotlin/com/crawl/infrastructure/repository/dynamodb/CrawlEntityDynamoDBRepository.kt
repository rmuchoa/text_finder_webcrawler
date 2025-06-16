package com.crawl.infrastructure.repository.dynamodb

import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class CrawlEntityDynamoDBRepository(
    val dynamoDbEnhancedClient: DynamoDbEnhancedClient) {

    private val table: DynamoDbTable<CrawlEntity>
        get() = dynamoDbEnhancedClient.table("CrawlEntity",
            TableSchema.fromBean(CrawlEntity::class.java))

    fun save(crawlEntity: CrawlEntity) {
        table.putItem(crawlEntity)
    }

}