package com.crawl.infrastructure.repository.dynamodb

import com.crawl.domain.values.Id
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class ScrapeEntityDynamoDBRepository(
    val dynamoDbEnhancedClient: DynamoDbEnhancedClient) {

    private val table: DynamoDbTable<ScrapeEntity>
        get() = dynamoDbEnhancedClient.table("ScrapeEntity",
            TableSchema.fromBean(ScrapeEntity::class.java))

    fun save(scrapeEntity: ScrapeEntity) {
        table.putItem(scrapeEntity)
    }

    fun findById(scrapeId: Id): ScrapeEntity? {
        return table.getItem {
            it.key { k ->
                k.partitionValue(scrapeId.toString())
            }
        }
    }

}