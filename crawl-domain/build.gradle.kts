plugins {
    id("buildsrc.convention.kotlin-jvm")
    `java-test-fixtures`

    alias(libs.plugins.kotlinPluginSerialization)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.bundles.kotlinxEcosystem)
    api("org.jetbrains.kotlin:kotlin-reflect")
    api(libs.slf4jModule)

    testImplementation(libs.bundles.junitTestEcosystem)
    testRuntimeOnly(libs.junitJupiterEngine)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}