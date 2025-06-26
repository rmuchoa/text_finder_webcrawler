package com.crawl.infrastructure.notification

import com.crawl.application.port.input.CrawlWaveReleaserPort
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CrawlNotificationReceiver(
    val waveProcessorPort: CrawlWaveReleaserPort) {

    var log: Logger = LoggerFactory.getLogger(CrawlNotificationReceiver::class.java)

    fun receive(message: String) {

        val notification = Json.decodeFromString<CrawlNotificationDTO>(string = message)
        log.info("Notification received from crawl {}", notification.id)

        waveProcessorPort.releaseWave(notification)

    }

}