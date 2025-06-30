package com.crawl.infrastructure.task

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class WebCrawlerStartupRunner(
    val taskRunner: WebCrawlerTaskRunner
) : ApplicationRunner {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun run(args: ApplicationArguments?) {

        log.info("WEB CRAWLER STARTUP: Starting web crowler tasks!")
        taskRunner.runCrawlingTasks()
    }

}