package com.crawl.domain.exception

class InvalidKeywordException(
    message: String,
    cause: Throwable? = null):
    RuntimeException(message, cause)