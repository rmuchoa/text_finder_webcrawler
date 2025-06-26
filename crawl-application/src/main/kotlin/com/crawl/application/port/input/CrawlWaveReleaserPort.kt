package com.crawl.application.port.input

import com.crawl.application.notification.CrawlNotification

interface CrawlWaveReleaserPort {
    fun releaseWave(notification: CrawlNotification)
}