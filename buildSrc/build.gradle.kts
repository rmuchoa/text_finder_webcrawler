plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinGradlePlugin)

    testImplementation(libs.bundles.junitTestEcosystem)
    testRuntimeOnly(libs.junitJupiterEngine)
}
