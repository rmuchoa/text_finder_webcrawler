package com.crawl.domain.values

import com.crawl.domain.util.RandomStringUtils

@ConsistentCopyVisibility
data class RandomId internal constructor(val id: CrawlId) {

    fun getId(): Id = id.id

    override fun toString(): String = id.toString()

    companion object {
        internal const val RANDOM_ID_MAX_LENGTH: Long = 8L

        fun of() = RandomId(id = CrawlId.Companion.generateCrawlRandomId())
    }
}

private fun CrawlId.Companion.generateCrawlRandomId() =
    CrawlId.of(id = RandomId.Companion.generateRandomId())

private fun RandomId.Companion.generateRandomId() =
    RandomStringUtils.getRandomAlfanumericString(maxLength = RANDOM_ID_MAX_LENGTH)