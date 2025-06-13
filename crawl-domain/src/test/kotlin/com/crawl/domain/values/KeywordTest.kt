package com.crawl.domain.values

import com.crawl.domain.AbstractTest
import com.crawl.domain.exception.InvalidKeywordException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class KeywordTest : AbstractTest() {

    @Test
    @DisplayName("Deve lançar uma InvalidKeywordException quando tentar criar um Keyword passando um valor nulo como keyword")
    fun shouldThrowInvalidKeywordExceptionWhenCreateAKeywordWithNullValue() {
        val thrown: InvalidKeywordException? = assertThrows(
            InvalidKeywordException::class.java,
            Executable { Keyword.of(null) })

        assertThat(thrown,
            hasProperty("message",
                equalTo("field 'keyword' is required (from 4 up to 32 chars)")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidKeywordException quando tentar criar um Keyword passando uma String vazia como keyword")
    fun shouldThrowInvalidKeywordExceptionWhenCreateAKeywordWithBlankStringValue() {
        val thrown: InvalidKeywordException? = assertThrows(
            InvalidKeywordException::class.java,
            Executable { Keyword.of("") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("field 'keyword' is required (from 4 up to 32 chars)")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidKeywordException quando tentar criar um Keyword passando uma keyword menor do que 4 caractéres")
    fun shouldThrowInvalidKeywordExceptionWhenCreateAKeywordWithStringValueShorterThanFourCharacters() {
        val thrown: InvalidKeywordException? = assertThrows(
            InvalidKeywordException::class.java,
            Executable { Keyword.of("012") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("field 'keyword' is required (from 4 up to 32 chars)")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidKeywordException quando tentar criar um Keyword passando uma keyword maior do que 32 caractéres")
    fun shouldThrowInvalidKeywordExceptionWhenCreateAKeywordWithStringValueLongerThanThirtyTwoCharacters() {
        val thrown: InvalidKeywordException? = assertThrows(
            InvalidKeywordException::class.java,
            Executable { Keyword.of("Lorem ipsum dolor sit amet. Et quam magnam rem atque aliquam.") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("field 'keyword' is required (from 4 up to 32 chars)")))
    }

    @Test
    @DisplayName("Deve criar um objeto Keyword contendo a keyword informada como String")
    fun shouldCreateKeywordWithValidKeywordValue() {
        val keyword = Keyword.of(defaultKeyword)

        assertThat(keyword,
            hasProperty("keyword",
                equalTo(defaultKeyword)))
    }

}