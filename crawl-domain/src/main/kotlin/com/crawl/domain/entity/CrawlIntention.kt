package com.crawl.domain.entity

import com.crawl.domain.values.CrawlStatus
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.RandomId

@ConsistentCopyVisibility
data class CrawlIntention internal constructor(
    val id: RandomId,
    val status: CrawlStatus,
    val keyword: Keyword) {

    fun getId() = id.getId()

    companion object{
        fun of(keywordValue: String?) = CrawlIntention(
            RandomId.of(),
            CrawlStatus.ACTIVE,
            Keyword.of(keywordValue))
    }
}