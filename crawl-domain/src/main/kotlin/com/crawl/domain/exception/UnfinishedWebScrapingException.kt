package com.crawl.domain.exception

class UnfinishedWebScrapingException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)