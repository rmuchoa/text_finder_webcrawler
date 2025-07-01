package com.crawl.domain.exception

import kotlin.RuntimeException

class ScrapeNotFoundException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)