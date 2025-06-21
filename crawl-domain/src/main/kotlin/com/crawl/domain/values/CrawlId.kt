package com.crawl.domain.values

import com.crawl.domain.exception.InvalidIdException

@ConsistentCopyVisibility
data class CrawlId internal constructor(val id: Id) {

    override fun toString(): String = id.toString()

    companion object {
        fun of(id: String?) = CrawlId(id = validId(id))

        private fun validId(id: String?) =
            Id.of(id = nonAlphanumericMath(
                id = nonEightCharSize(
                    id = nonBlankId(id))))

        private fun nonBlankId(id: String?) =
            id?.takeIf { it.isNotBlank() }
                ?: throw InvalidIdException(message = "Crawl id cannot be empty!")

        private fun nonEightCharSize(id: String?) =
            id?.takeIf { it.length == 8 }
                ?: throw InvalidIdException(message = "Invalid crawl id with length different than 8 character!")

        private fun nonAlphanumericMath(id: String?) =
            id?.takeIf { it.matches(regex = "[a-zA-Z0-9]*".toRegex()) }
                ?: throw InvalidIdException(message = "Invalid crawl id with characters different than letters and numbers!")
    }
}
