package com.crawl.application.port.input

import com.crawl.domain.values.Id

interface CrawlWaveReleaserPort {
    fun releaseWave(crawlId: Id)
}