package com.crawl.domain.entity

import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url

data class ScrapeResult(
    val keyword: Keyword,
    val partialResult: Boolean,
    val scrapedUrlsCount: Int?,
    var scrapedUrls: List<Url>?) {

    fun hasFinishedResult(): Boolean {
        return !partialResult
    }

    fun getExistentScrapedUrls(): List<Url> {
        return scrapedUrls?.takeIf { it.isNotEmpty() } ?: listOf()
    }

    companion object {
        fun of(
            keyword: Keyword,
            partialResult: Boolean,
            scrapedUrls: List<Url>?) = ScrapeResult(
                keyword = keyword,
                partialResult = partialResult,
                scrapedUrlsCount = scrapedUrls?.size,
                scrapedUrls = scrapedUrls
            )
    }

}
