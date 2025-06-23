package com.crawl.infrastructure.repository.dynamodb

import com.crawl.domain.values.Id
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

    fun findById(crawlId: Id): CrawlEntity? {
        return table.getItem {
            it.key { k ->
                k.partitionValue(crawlId.toString())
            }
        }
    }

}