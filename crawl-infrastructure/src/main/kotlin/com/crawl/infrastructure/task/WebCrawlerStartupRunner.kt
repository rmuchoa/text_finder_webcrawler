package com.crawl.infrastructure.task

import com.crawl.infrastructure.configuration.WebCrawlerTaskConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext

@Component
class WebCrawlerStartupRunner(
    val taskRunner: WebCrawlerTaskRunner,
    val dispatcher: CoroutineDispatcher
) : ApplicationRunner {

    var log: Logger = LoggerFactory.getLogger(this::class.java)
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log.error("Unhandled throwable in coroutine: ${throwable.message}", throwable)
    }

    fun getContext(): CoroutineContext {
        return dispatcher + exceptionHandler
    }

    override fun run(args: ApplicationArguments?) {

        log.info("WEB CRAWLER STARTUP: Starting web crowler tasks!")
        CoroutineScope(context = getContext()).launch {
            repeat(times = WebCrawlerTaskConfiguration.MAX_TASKS_CAPACITY) { taskId ->
                taskRunner.runCrawlingTask(taskId = taskId+1)
            }
        }
    }

}