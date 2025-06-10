plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.kotlinPluginSerialization)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.bundles.kotlinxEcosystem)
    api("org.jetbrains.kotlin:kotlin-reflect")

    testApi(libs.kotlinTestJunit5)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}