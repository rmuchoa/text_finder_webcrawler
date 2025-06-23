package com.crawl.domain.exception

import kotlin.RuntimeException

class CrawlNotFoundException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)