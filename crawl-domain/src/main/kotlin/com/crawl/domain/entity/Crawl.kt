package com.crawl.domain.entity

import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Id
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url

@ConsistentCopyVisibility
data class Crawl internal constructor(
    val id: Id,
    var status: CrawlStatus,
    val keyword: Keyword,
    var result: CrawlResult?) {

    fun applyResults(result: CrawlResult) {

        this@Crawl.result = result

        if (result.hasFinishedResult()) status = CrawlStatus.DONE

    }

    companion object{
        fun of(
            id: String,
            status: String,
            keyword: String,
            partialResult: Boolean,
            scrapedUrls: List<String>) = Crawl(
            id = Id.of(id),
            status = CrawlStatus.of(status),
            keyword = Keyword.of(keyword),
            result = CrawlResult.of(
                keyword = Keyword.of(keyword),
                partialResult = partialResult,
                scrapedUrls = scrapedUrls.map { Url.of(url = it) }))
    }

}
