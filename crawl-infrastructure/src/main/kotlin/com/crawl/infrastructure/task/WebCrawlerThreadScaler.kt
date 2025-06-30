package com.crawl.infrastructure.task

import com.crawl.infrastructure.configuration.WebCrawlerTaskConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class WebCrawlerThreadScaler(val taskExecutor: ThreadPoolTaskExecutor) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = WebCrawlerTaskConfiguration.TEN_SECONDS_DELAY)
    fun scaleUp() {
        if (taskExecutor.corePoolSize < taskExecutor.maxPoolSize) {
            taskExecutor.corePoolSize += 1
            log.info("WEB CRAWLER POOL: Gradually scaling web crawler pool for {} threads", taskExecutor.corePoolSize)
        }
    }

}