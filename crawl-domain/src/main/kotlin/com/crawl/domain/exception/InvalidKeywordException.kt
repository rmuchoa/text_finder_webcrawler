package com.crawl.domain.exception

class InvalidKeywordException(
    message: String,
    causa: Throwable? = null) : RuntimeException(message, causa)