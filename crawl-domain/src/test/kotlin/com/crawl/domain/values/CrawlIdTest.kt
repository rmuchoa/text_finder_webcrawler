package com.crawl.domain.values

import com.crawl.domain.AbstractTest
import com.crawl.domain.exception.InvalidIdException
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
class CrawlIdTest : AbstractTest() {

    @Test
    @DisplayName("Deve lançar uma InvalidIdException quando tentar criar um CrawlId passando um id nulo")
    fun shouldThrowInvalidIdExceptionWhenCreateAnIdWithNullValue() {
        val thrown: InvalidIdException? = assertThrows(
            InvalidIdException::class.java,
            Executable { CrawlId.of(null) })

        assertThat(thrown,
            hasProperty("message",
                equalTo("Crawl id cannot be empty!")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidIdException quando tentar criar um CrawlId passando um id em branco")
    fun shouldThrowInvalidIdExceptionWhenCreateAnIdWithBlankStringValue() {
        val thrown: InvalidIdException? = assertThrows(
            InvalidIdException::class.java,
            Executable { CrawlId.of("") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("Crawl id cannot be empty!")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidIdException quando tentar criar um CrawlId passando um id com menos do que 8 caractéres")
    fun shouldThrowInvalidIdExceptionWhenCreateAnIdWithStringValueShorterThanEightCharacters() {
        val thrown: InvalidIdException? = assertThrows(
            InvalidIdException::class.java,
            Executable { CrawlId.of("012345") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("Invalid crawl id with length different than 8 character!")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidIdException quando tentar criar um CrawlId passando um id com mais do que 8 caractéres")
    fun shouldThrowInvalidIdExceptionWhenCreateAnIdWithStringValueLongerThanEightCharacters() {
        val thrown: InvalidIdException? = assertThrows(
            InvalidIdException::class.java,
            Executable { CrawlId.of("0123456789") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("Invalid crawl id with length different than 8 character!")))
    }

    @Test
    @DisplayName("Deve lançar uma InvalidIdException quando tentar criar um CrawlId passando um id que contenha algum caractére especial")
    fun shouldThrowInvalidIdExceptionWhenCreateAnIdWithStringValueThatContainsSpecialCharacters() {
        val thrown: InvalidIdException? = assertThrows(
            InvalidIdException::class.java,
            Executable { CrawlId.of("|2&4$678") })

        assertThat(thrown,
            hasProperty("message",
                equalTo("Invalid crawl id with characters different than letters and numbers!")))
    }

    @Test
    @DisplayName("Deve criar um objeto CrawlId contendo o id informado como String")
    fun shouldCreateIdWithValidIdValue() {
        val crawlId = CrawlId.of(defaultId)

        assertThat(crawlId,
            hasProperty("id",
                equalTo(crawlId.id)))
    }

}