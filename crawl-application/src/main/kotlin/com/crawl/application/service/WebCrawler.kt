package com.crawl.application.service

import com.crawl.domain.port.output.WebCrawlerPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebCrawler() : WebCrawlerPort {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun executeCrawl() {

    }
}