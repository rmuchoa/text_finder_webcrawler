package com.crawl.infrastructure.api

import com.crawl.application.dto.CrawlRequest
import com.crawl.application.dto.RequestedCrawl
import com.crawl.application.port.input.CrawlInputPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crawl")
class CrawlAPIController(val inputPort: CrawlInputPort) {

    @PostMapping()
    fun requestCrawl(@RequestBody request: CrawlRequest): ResponseEntity<RequestedCrawl> {
        val requestedCrawl: RequestedCrawl? = inputPort.requestCrawl(request)

        return requestedCrawl?.let {
            ResponseEntity.ok(requestedCrawl)
        } ?: ResponseEntity.notFound().build()
    }

}