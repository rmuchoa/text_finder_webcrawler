package com.crawl.infrastructure.repository.dinamodb

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntity
import com.crawl.infrastructure.repository.dynamodb.ScrapeEntityDynamoDBRepository
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
class ScrapeEntityDynamoDBRepositoryTest : AbstractTest() {

    @Mock private lateinit var dynamoDbEnhancedClient: DynamoDbEnhancedClient
    @Mock private lateinit var table: DynamoDbTable<ScrapeEntity>
    private lateinit var dynamoDbRepository: ScrapeEntityDynamoDBRepository

    @BeforeEach
    fun setUp() {
        dynamoDbRepository = ScrapeEntityDynamoDBRepository(dynamoDbEnhancedClient)
        `when`(dynamoDbEnhancedClient.table(eq("ScrapeEntity"),
            eq(TableSchema.fromBean(ScrapeEntity::class.java)))).thenReturn(table)
    }

    @Test
    @DisplayName("Deve pedir à tabela de ScrapeEntity para inserir um item ScrapeEntity quando o repositório dynamodb salvar uma ScrapeEntity")
    fun shouldAskScrapeEntityTableToPutGivenScrapeEntityItemWhenSavingScrapeEntityOnDynamoDBRepository() {
        val scrapeEntity = ScrapeEntity.of(Id.of(defaultId), ScrapeStatus.ACTIVE, Keyword.of(defaultKeyword))

        dynamoDbRepository.save(scrapeEntity)

        verify(table, atLeastOnce()).putItem(eq(scrapeEntity))
    }

}