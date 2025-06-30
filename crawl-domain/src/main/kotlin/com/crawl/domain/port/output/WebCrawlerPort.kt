package com.crawl.domain.port.output

interface WebCrawlerPort {
    fun executeCrawl(taskId: Int)
}