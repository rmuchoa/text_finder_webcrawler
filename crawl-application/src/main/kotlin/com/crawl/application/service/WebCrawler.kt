package com.crawl.application.service

import com.crawl.domain.port.output.WebCrawlerPort
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebCrawler : WebCrawlerPort {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun executeCrawl(taskId: Int) {
        if (taskId % 5 == 0) {
            throw IllegalStateException("Failed on task $taskId!")
        }
        log.info("-------------------> Executing Task {} STARTED!", taskId)
        Thread.sleep(5000)
        log.info("-------------------> Executing Task {} FINISHED!", taskId)
    }
}