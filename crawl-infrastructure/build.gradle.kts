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

    implementation(project(":crawl-application"))

    testImplementation(libs.springBootStarterTest)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.named("bootJar") {
    enabled = false
}