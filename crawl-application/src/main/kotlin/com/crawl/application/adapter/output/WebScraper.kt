package com.crawl.application.adapter.output

import com.crawl.application.port.output.VisitedUrlRepositoryPort
import com.crawl.application.service.WebPageFinder
import com.crawl.domain.entity.ScrapeResult
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.port.output.WebScraperPort
import com.crawl.domain.values.Keyword
import com.crawl.domain.values.Url
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class WebScraper(
    val webPageFinder: WebPageFinder,
    val visitedUrlRepository: VisitedUrlRepositoryPort
) : WebScraperPort {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun startKeywordScraping(keyword: Keyword): ScrapeResult {

        val allVisitedUrl: List<Url> = visitedUrlRepository.listAllVisitedUrls()

        log.info("WEB SCRAPER: Starting to scrape for keyword {} on loaded web pages! crawledUrls {}", keyword, allVisitedUrl.size)

        val result = ScrapeResult.of(
            keyword,
            partialResult = visitedUrlRepository.doesNotFinishedNavigationYet(),
            scrapedUrls = allVisitedUrl
                .filter { url: Url -> scrapeKeywordWebPage(keyword, url) })

        log.info("WEB SCRAPER: Finished scraping for keyword {} on loaded web pages! scrapedUrls {}", keyword, result.scrapedUrlsCount)

        return result
    }

    private fun scrapeKeywordWebPage(keyword: Keyword, url: Url): Boolean {

        log.debug("WEB SCRAPER: Starting to scrape for keyword {} on url {}!", keyword, url)
        val webPage: WebPageDocument? = webPageFinder.findWebPageByUrl(url)

        return webPage?.let {
            val scrapingSuccess: Boolean = it.scrapeKeyword(keyword)
            log.debug("WEB SCRAPER: Finished url scraping! url: {}", url)
            scrapingSuccess

        }?: run {
            log.debug("WEB SCRAPER: Couldn't find web page to scrape keyword! url: {}", url);
            false
        }
    }

}