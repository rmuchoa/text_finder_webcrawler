package com.crawl.domain.values

import com.crawl.domain.exception.InvalidKeywordException

@ConsistentCopyVisibility
data class Keyword internal constructor(val keyword: String) {

    override fun toString(): String = keyword

    companion object {
        fun of(keyword: String?) = Keyword(validKeyword(keyword))

        private fun validKeyword(keyword: String?) =
            keyword?.takeIf { it.isNotBlank()
                    && nonSmallerThanFourChars(it)
                    && nonGreaterThanThirtyTwoChars(it) }
                ?: throw InvalidKeywordException("field 'keyword' is required (from 4 up to 32 chars)")

        private fun nonSmallerThanFourChars(keyword: String) =
            keyword.length >= 4

        private fun nonGreaterThanThirtyTwoChars(keyword: String) =
            keyword.length <= 32
    }
}
