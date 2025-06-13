package com.crawl.domain.exception

class InvalidIdException(
    message: String,
    causa: Throwable? = null) : RuntimeException(message, causa)