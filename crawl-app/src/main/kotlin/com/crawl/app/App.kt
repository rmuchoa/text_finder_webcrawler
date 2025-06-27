package com.crawl.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = [
    "com.crawl.infrastructure.*"])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}