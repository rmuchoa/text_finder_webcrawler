package com.crawl.infrastructure.api

import com.crawl.application.dto.ScrapeRequest
import com.crawl.application.dto.RequestedScrape
import com.crawl.application.port.input.ScrapeInputPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crawl")
class CrawlAPIController(val inputPort: ScrapeInputPort) {

    @PostMapping()
    fun requestCrawl(@RequestBody request: ScrapeRequest): ResponseEntity<RequestedScrape> {
        val requestedScrape: RequestedScrape? = inputPort.requestScrape(request)

        return requestedScrape?.let {
            ResponseEntity.ok(requestedScrape)
        } ?: ResponseEntity.notFound().build()
    }

}