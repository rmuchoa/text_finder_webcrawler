plugins {
    id("buildsrc.convention.kotlin-jvm")

    application
}

dependencies {
    implementation(project(":crawl-infrastructure"))
}

application {
    mainClass = "com.web.app.AppKt"
}
