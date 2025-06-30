package com.crawl.application.service

import com.crawl.domain.port.output.WebCrawlerPort

open class WebCrawler : WebCrawlerPort {

    override fun executeCrawl() {
        Thread.sleep(30000)
    }
}