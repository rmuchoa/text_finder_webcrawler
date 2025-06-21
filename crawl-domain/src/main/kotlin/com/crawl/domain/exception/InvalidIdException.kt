package com.crawl.domain.exception

class InvalidIdException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)