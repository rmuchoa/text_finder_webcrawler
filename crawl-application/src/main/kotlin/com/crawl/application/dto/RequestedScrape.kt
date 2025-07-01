package com.crawl.application.dto

import com.crawl.domain.values.Id

@ConsistentCopyVisibility
data class RequestedScrape internal constructor(val id: String) {

    companion object {
        fun of(id: Id) = RequestedScrape(id.toString())
    }
}
