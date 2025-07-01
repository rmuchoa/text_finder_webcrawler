package com.crawl.domain.entity

import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url

@ConsistentCopyVisibility
data class Scrape internal constructor(
    val id: Id,
    var status: ScrapeStatus,
    val keyword: Keyword,
    var result: ScrapeResult?) {

    fun applyResults(result: ScrapeResult) {

        this@Scrape.result = result

        if (result.hasFinishedResult()) status = ScrapeStatus.DONE

    }

    companion object{
        fun of(
            id: String,
            status: String,
            keyword: String,
            partialResult: Boolean,
            scrapedUrls: List<String>) = Scrape(
            id = Id.of(id),
            status = ScrapeStatus.of(status),
            keyword = Keyword.of(keyword),
            result = ScrapeResult.of(
                keyword = Keyword.of(keyword),
                partialResult = partialResult,
                scrapedUrls = scrapedUrls.map { Url.of(url = it) }))
    }

}
