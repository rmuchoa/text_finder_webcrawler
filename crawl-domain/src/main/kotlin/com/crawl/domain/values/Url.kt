package com.crawl.domain.values

@ConsistentCopyVisibility
data class Url internal constructor(val url: String) {

    override fun toString(): String = url

    companion object {
        fun of(url: String) = Url(url = url)
    }

}
