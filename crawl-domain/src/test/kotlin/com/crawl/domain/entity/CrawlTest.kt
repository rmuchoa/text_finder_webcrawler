package com.crawl.domain.entity

import com.crawl.domain.AbstractTest
import com.crawl.domain.values.CrawlStatus
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
class CrawlTest : AbstractTest() {

    @Test
    @DisplayName("Deve aplicar resultados no Crawl sem mudar status quando o CrawlResult fornecido tem um resultado parcial")
    fun shouldApplyResultsOnCrawlWithoutChangeStatusWhenGivenCrawlResultHasPartialResult() {
        val url = Url.of(url = myLink)
        val crawl = Crawl.of(
            id = defaultId,
            status = CrawlStatus.ACTIVE.status,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls = listOf()
        )
        val crawlResult = CrawlResult.of(
            keyword = crawl.keyword,
            partialResult,
            scrapedUrls = listOf(url)
        )

        crawl.applyResults(crawlResult)

        assertThat(crawl, allOf(
            instanceOf(Crawl::class.java),
            hasProperty("status",
                equalTo(CrawlStatus.ACTIVE)),
            hasProperty("result", allOf(
                instanceOf<CrawlResult>(CrawlResult::class.java),
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
    @DisplayName("Deve aplicar DONE status ao aplicar resultados quando o CrawlResult fornecido tem um resultado não parcial")
    fun shouldApplyDoneStatusOnApplyResultsWhenGivenCrawlResultHasNotPartialResult() {
        val url = Url.of(url = myLink)
        val crawl = Crawl.of(
            id = defaultId,
            status = CrawlStatus.ACTIVE.status,
            keyword = defaultKeyword,
            partialResult,
            scrapedUrls = listOf(url.url)
        )
        val crawlResult = CrawlResult.of(
            keyword = crawl.keyword,
            notPartialResult,
            scrapedUrls = listOf(url)
        )

        crawl.applyResults(crawlResult)

        assertThat(crawl, allOf(
            instanceOf(Crawl::class.java),
            hasProperty("status",
                equalTo(CrawlStatus.DONE)),
            hasProperty("result", allOf(
                instanceOf<CrawlResult>(CrawlResult::class.java),
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