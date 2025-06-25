package com.crawl.domain.entity

import com.crawl.domain.values.Keyword

interface WebPage {
    fun hasHtmlElement(): Boolean
    fun hasBodyElement(): Boolean
    fun containsKeyword(keyword: Keyword): Boolean
}