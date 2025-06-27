package com.crawl.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
open class WebCrawlerTaskConfiguration {

    @Bean
    open fun taskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = START_FROM_SCRATCH
            maxPoolSize = MAX_THREADS_ATTEMPTED
            setQueueCapacity(MAX_TASKS_CAPACITY)
            setThreadNamePrefix("Crawler-")
            initialize()
        }
    }

    companion object {
        const val MAX_TASKS_CAPACITY: Int = 30
        const val MAX_THREADS_ATTEMPTED: Int = 6
        const val START_FROM_SCRATCH: Int = 0
        const val TEN_SECONDS_DELAY: Long = 10000
    }

}