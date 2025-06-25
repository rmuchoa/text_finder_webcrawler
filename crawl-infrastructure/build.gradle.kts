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
    api(libs.springBootStarter)
    api(libs.springCloudAwsSqsStarter)
    api(libs.springCloudAwsDynamodbStarter)
    api(libs.jacksonKotlinModule)
    implementation(libs.jsoupModule)

    implementation(project(":crawl-application"))
    implementation(project(":crawl-domain"))
    testImplementation(testFixtures(project(":crawl-domain")))

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.bundles.junitTestEcosystem)
    testRuntimeOnly(libs.junitJupiterEngine)
}

tasks.named("bootJar") {
    enabled = false
}

tasks.named("jar") {
    enabled = true
}