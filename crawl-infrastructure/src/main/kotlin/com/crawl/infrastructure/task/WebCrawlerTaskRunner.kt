package com.crawl.infrastructure.task

import com.crawl.application.service.WebCrawler
import com.crawl.infrastructure.configuration.WebCrawlerTaskConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class WebCrawlerTaskRunner(
    val taskExecutor: ThreadPoolTaskExecutor,
    val webCrawler: WebCrawler
) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun runCrawlingTasks() {

        repeat(times = WebCrawlerTaskConfiguration.MAX_TASKS_CAPACITY) { taskId ->
            taskExecutor.execute {
                try {

                    log.info("WEB CRAWLER TASK RUNNER: Executing web crawling on thread ${Thread.currentThread().name}")
                    webCrawler.executeCrawl(taskId)

                } catch (exception: Exception) {
                    log.error("WEB CRAWLER TASK RUNNER: Some error occurred on web crawling on thread ${Thread.currentThread().name}: ${exception.message}")
                }
            }
        }
    }

}