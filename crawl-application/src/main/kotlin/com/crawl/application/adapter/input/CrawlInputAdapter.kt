package com.crawl.application.adapter.input

import com.crawl.application.dto.CrawlRequest
import com.crawl.application.dto.RequestedCrawl
import com.crawl.application.port.input.CrawlInputPort
import com.crawl.domain.entity.CrawlIntention
import com.crawl.domain.service.CrawlIntentor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class CrawlInputAdapter(
    val crawlIntentor: CrawlIntentor):
    CrawlInputPort {

    val log: Logger = LoggerFactory.getLogger(CrawlInputAdapter::class.java)

    override fun requestCrawl(request: CrawlRequest): RequestedCrawl {

        log.debug("CRAWL: Treating a crawl requisition by keyword ${request.keyword}!")

        val crawlIntention: CrawlIntention = CrawlIntention.of(keywordValue = request.keyword)
        log.info("CRAWL: Created a crawl intention with id ${crawlIntention.id}! keyword: ${crawlIntention.keyword}")

        val startedCrawl: CrawlIntention = crawlIntentor.intent(crawlIntention)
        log.debug("CRAWL: Registered a crawl requisition by keyword ${request.keyword}!")

        return RequestedCrawl.of(id = startedCrawl.getId());
    }

}