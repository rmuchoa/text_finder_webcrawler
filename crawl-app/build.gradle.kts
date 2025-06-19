plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.springBootPlugin)

    application
}

dependencies {
    implementation(project(":crawl-infrastructure"))
}

application {
    mainClass.set("com.crawl.app.AppKt")
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

tasks.jar {
    enabled = false
}
