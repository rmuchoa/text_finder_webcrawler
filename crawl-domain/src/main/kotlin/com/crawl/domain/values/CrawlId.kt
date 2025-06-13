package com.crawl.domain.values

import com.crawl.domain.exception.InvalidIdException

@ConsistentCopyVisibility
data class CrawlId internal constructor(val id: Id) {

    override fun toString(): String = id.toString()

    companion object {
        fun of(id: String?) = CrawlId(validId(id))

        private fun validId(id: String?) =
            Id.of(nonAlphanumericMath(
                nonEightCharSize(
                    nonBlankId(id))))

        private fun nonBlankId(id: String?) =
            id?.takeIf { it.isNotBlank() }
                ?: throw InvalidIdException("Crawl id cannot be empty!")

        private fun nonEightCharSize(id: String?) =
            id?.takeIf { it.length == 8 }
                ?: throw InvalidIdException("Invalid crawl id with length different than 8 character!")

        private fun nonAlphanumericMath(id: String?) =
            id?.takeIf { it.matches("[a-zA-Z0-9]*".toRegex()) }
                ?: throw InvalidIdException("Invalid crawl id with characters different than letters and numbers!")
    }
}
