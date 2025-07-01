package com.crawl.domain.entity

import com.crawl.domain.values.ScrapeStatus
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.RandomId

@ConsistentCopyVisibility
data class ScrapeIntention internal constructor(
    val id: RandomId,
    val status: ScrapeStatus,
    val keyword: Keyword) {

    fun getId() = id.getId()

    companion object{
        fun of(keywordValue: String?) = ScrapeIntention(
            id = RandomId.of(),
            status = ScrapeStatus.ACTIVE,
            Keyword.of(keyword = keywordValue))
    }
}