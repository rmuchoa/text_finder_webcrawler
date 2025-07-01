package com.crawl.infrastructure.notification

import com.crawl.application.port.input.ScrapeWaveReleaserPort
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ScrapeNotificationReceiver(
    val waveProcessorPort: ScrapeWaveReleaserPort) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun receive(message: String) {

        val notification = Json.decodeFromString<ScrapeNotificationDTO>(string = message)
        log.info("Notification received from scrape {}", notification.id)

        waveProcessorPort.releaseWave(notification)

    }

}