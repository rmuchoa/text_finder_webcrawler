package com.crawl.domain.values

import com.crawl.domain.values.CrawlStatus.entries

enum class CrawlStatus(val status: String) {
    ACTIVE("active"),
    DONE("done");

    companion object {
        fun of(status: String?): CrawlStatus? {
            if (status == null)
                return null;

            return entries.find {
                it.name.equals(status, ignoreCase = true) }
        }
    }
}
