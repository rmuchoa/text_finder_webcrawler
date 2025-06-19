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
    api(libs.awsSdkDynamodbEnhanceModule)
    api(libs.awsSdkDynamodbModule)
    api(libs.awsSdkSqsModule)
    api(libs.jacksonKotlinModule)

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