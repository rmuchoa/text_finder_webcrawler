package com.crawl.domain.values

enum class CrawlStatus(val status: String) {
    ACTIVE("active"),
    DONE("done");

    override fun toString(): String = status

    companion object {
        fun of(status: String?): CrawlStatus? {
            if (status == null)
                return null;

            return entries.find {
                it.name.equals(status, ignoreCase = true) }
        }
    }
}
