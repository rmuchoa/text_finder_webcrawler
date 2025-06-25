package com.crawl.domain.exception

class InvalidWebPageException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)