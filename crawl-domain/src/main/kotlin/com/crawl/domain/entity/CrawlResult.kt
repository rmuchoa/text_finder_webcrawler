package com.crawl.domain.entity

import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import java.util.Arrays.asList

data class CrawlResult(
    val keyword: Keyword,
    val partialResult: Boolean,
    val scrapedUrlsCount: Int?,
    var scrapedUrls: List<Url>?) {

    fun hasFinishedResult(): Boolean {
        return !partialResult
    }

    fun getExistentScrapedUrls(): List<Url> {
        return scrapedUrls?.takeIf { it.isNotEmpty() } ?: asList()
    }

    companion object {
        fun of(
            keyword: Keyword,
            partialResult: Boolean,
            scrapedUrls: List<Url>?) = CrawlResult(
                keyword = keyword,
                partialResult = partialResult,
                scrapedUrlsCount = scrapedUrls?.size,
                scrapedUrls = scrapedUrls
            )
    }

}
