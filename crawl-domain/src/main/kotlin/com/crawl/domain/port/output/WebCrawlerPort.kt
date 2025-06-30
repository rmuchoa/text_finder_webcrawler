package com.crawl.domain.port.output

interface WebCrawlerPort {
    suspend fun executeCrawl(taskId: Int)
}