package com.crawl.domain.values

@ConsistentCopyVisibility
data class Id internal constructor(val id: String) {

    override fun toString(): String = id

    companion object {
        fun of(id: String) = Id(id)
    }
}