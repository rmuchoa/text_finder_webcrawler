package com.crawl.domain.entity

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ScrapeTest : AbstractTest() {

    @Test
    @DisplayName("Deve aplicar resultados no Scrape sem mudar status quando o ScrapeResult fornecido tem um resultado parcial")
    fun shouldApplyResultsOnScrapeWithoutChangeStatusWhenGivenScrapeResultHasPartialResult() {
        val url = Url.of(url = myLink)
        val scrape = Scrape.of(
            id = defaultId,
            status = ScrapeStatus.ACTIVE.status,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls = listOf()
        )
        val scrapeResult = ScrapeResult.of(
            keyword = scrape.keyword,
            partialResult,
            scrapedUrls = listOf(url)
        )

        scrape.applyResults(scrapeResult)

        assertThat(scrape, allOf(
            instanceOf(Scrape::class.java),
            hasProperty("status",
                equalTo(ScrapeStatus.ACTIVE)),
            hasProperty("result", allOf(
                instanceOf<ScrapeResult>(ScrapeResult::class.java),
                hasProperty("keyword",
                    instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(
                    instanceOf(Boolean::class.java), equalTo(partialResult))),
                hasProperty("scrapedUrls", allOf(
                    instanceOf(List::class.java), equalTo(listOf(url))))
            ))
        ))
    }

    @Test
    @DisplayName("Deve aplicar DONE status ao aplicar resultados quando o ScrapeResult fornecido tem um resultado não parcial")
    fun shouldApplyDoneStatusOnApplyResultsWhenGivenScrapeResultHasNotPartialResult() {
        val url = Url.of(url = myLink)
        val scrape = Scrape.of(
            id = defaultId,
            status = ScrapeStatus.ACTIVE.status,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls = listOf(url.url)
        )
        val scrapeResult = ScrapeResult.of(
            keyword = scrape.keyword,
            notPartialResult,
            scrapedUrls = listOf(url)
        )

        scrape.applyResults(scrapeResult)

        assertThat(scrape, allOf(
            instanceOf(Scrape::class.java),
            hasProperty("status",
                equalTo(ScrapeStatus.DONE)),
            hasProperty("result", allOf(
                instanceOf<ScrapeResult>(ScrapeResult::class.java),
                hasProperty("keyword",
                    instanceOf<Keyword>(Keyword::class.java)),
                hasProperty("partialResult", allOf(
                    instanceOf(Boolean::class.java), equalTo(notPartialResult))),
                hasProperty("scrapedUrls", allOf(
                    instanceOf(List::class.java), equalTo(listOf(url))))
            ))
        ))
    }

}