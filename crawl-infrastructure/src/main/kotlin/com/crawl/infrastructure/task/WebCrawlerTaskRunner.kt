package com.crawl.infrastructure.task

import com.crawl.application.service.WebCrawler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WebCrawlerTaskRunner(
    val dispatcher: CoroutineDispatcher,
    val webCrawler: WebCrawler
) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    suspend fun runCrawlingTask(taskId: Int) = supervisorScope {

        launch(context = dispatcher) {
            try {

                log.info("WEB CRAWLER TASK RUNNER: Executing web crawling task $taskId on thread ${Thread.currentThread().name}")
                webCrawler.executeCrawl(taskId)

            } catch (exception: Exception) {
                log.error("WEB CRAWLER TASK RUNNER: Some error occurred on web crawling task $taskId  on thread ${Thread.currentThread().name}: ${exception.message}")
            }
        }
    }

}