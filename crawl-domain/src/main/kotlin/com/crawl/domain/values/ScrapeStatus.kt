package com.crawl.domain.values

import com.crawl.domain.exception.InvalidStatusException

enum class ScrapeStatus(val status: String) {
    ACTIVE("active"),
    DONE("done");

    override fun toString(): String = status

    companion object {
        fun of(status: String?): ScrapeStatus {

            return entries.find {
                status != null && it.name.equals(other = status, ignoreCase = true)
            } ?: throw InvalidStatusException(
                message = "Invalid informed status not between active/done values")
        }
    }
}
