plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.kotlinPluginSerialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":crawl-domain"))

    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}