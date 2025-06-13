package com.crawl.domain.values

import com.crawl.domain.AbstractTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.hasLength
import org.hamcrest.Matchers.hasProperty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RandomIdTest : AbstractTest() {

    @Test
    @DisplayName("Deve criar um objeto RandomId contendo um id gerado com 8 caractéres")
    fun shouldCreateRandomIdWithAndResultantIdValueMustContainsExactlyEightRandomCharacters() {
        val randomId = RandomId.of()

        assertThat(randomId,
            hasProperty("id",
                allOf(
                    instanceOf(CrawlId::class.java),
                    hasProperty<Any?>("id",
                        allOf(
                            instanceOf(Id::class.java),
                            hasProperty<Any?>("id",
                                allOf(
                                    instanceOf(String::class.java),
                                    hasLength(8))))))))
    }

}