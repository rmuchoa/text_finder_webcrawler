package com.crawl.domain.exception

class InvalidStatusException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)