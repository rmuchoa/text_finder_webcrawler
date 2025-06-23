package com.crawl.domain.exception

class UnfinishedWebCrawlingException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)