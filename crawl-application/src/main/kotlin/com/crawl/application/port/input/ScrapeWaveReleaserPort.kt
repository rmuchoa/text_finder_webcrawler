package com.crawl.application.port.input

import com.crawl.application.notification.ScrapeNotification

interface ScrapeWaveReleaserPort {
    fun releaseWave(notification: ScrapeNotification)
}