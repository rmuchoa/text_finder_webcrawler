plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.springBootPlugin)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.springBootStarterWeb)
    api(libs.jacksonKotlinModule)

    implementation(project(":crawl-application"))
    implementation(project(":crawl-domain"))

    testImplementation(libs.springBootStarterTest)
}

tasks.named("bootJar") {
    enabled = false
}