dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":crawl-app")
include(":crawl-infrastructure")

rootProject.name = "text_finder_webcrawler"
include("crawl-application")
include("crawl-application")
include("crawl-domain")