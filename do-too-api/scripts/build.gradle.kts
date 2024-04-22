plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(20)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.postgres)
    implementation(libs.bundles.logging)

    implementation(project(":"))
}