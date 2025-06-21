package com.crawl.domain.util

object RandomStringUtils {

    private const val ALPHANUMERIC_SAMPLE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun getRandomAlfanumericString(maxLength: Long) =
        (1..maxLength)
            .map { ALPHANUMERIC_SAMPLE.random() }
            .joinToString(separator = "")
}