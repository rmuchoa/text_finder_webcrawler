plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.kotlinPluginSerialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":crawl-domain"))
    testImplementation(testFixtures(project(":crawl-domain")))

    testImplementation(libs.bundles.junitTestEcosystem)
    testRuntimeOnly(libs.junitJupiterEngine)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}