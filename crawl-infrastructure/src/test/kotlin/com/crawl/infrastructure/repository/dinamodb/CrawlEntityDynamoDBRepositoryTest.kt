package com.crawl.infrastructure.repository.dinamodb

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.infrastructure.repository.dynamodb.CrawlEntity
import com.crawl.infrastructure.repository.dynamodb.CrawlEntityDynamoDBRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@ExtendWith(MockitoExtension::class)
class CrawlEntityDynamoDBRepositoryTest : AbstractTest() {

    @Mock private lateinit var dynamoDbEnhancedClient: DynamoDbEnhancedClient
    @Mock private lateinit var table: DynamoDbTable<CrawlEntity>
    private lateinit var dynamoDbRepository: CrawlEntityDynamoDBRepository

    @BeforeEach
    fun setUp() {
        dynamoDbRepository = CrawlEntityDynamoDBRepository(dynamoDbEnhancedClient)
        `when`(dynamoDbEnhancedClient.table(eq("CrawlEntity"),
            eq(TableSchema.fromBean(CrawlEntity::class.java)))).thenReturn(table)
    }

    @Test
    @DisplayName("Deve pedir à tabela de CrawlEntity para inserir um item CrawlEntity quando o repositório dynamodb salvar uma CrawlEntity")
    fun shouldAskCrawlEntityTableToPutGivenCrawlEntityItemWhenSavingCrawlEntityOnDynamoDBRepository() {
        val crawlEntity = CrawlEntity.of(Id.of(defaultId), CrawlStatus.ACTIVE, Keyword.of(defaultKeyword))

        dynamoDbRepository.save(crawlEntity)

        verify(table, atLeastOnce()).putItem(eq(crawlEntity))
    }

}