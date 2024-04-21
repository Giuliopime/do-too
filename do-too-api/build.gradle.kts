plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ksp)
}

group = "dev.giuliopime"
version = "1.0"

application {
    mainClass.set("app.dotoo.ApplicationKt")
}

kotlin {
    jvmToolchain(20)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(libs.reflections)
    api(libs.slf4j.api)

    implementation(libs.kotlinx.datetime)

    ksp(libs.koin.ksp)
    implementation(libs.bundles.koin)

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.spring.security)
    implementation(libs.bundles.monitoring)

    implementation(libs.bundles.postgres)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.dotenv)
    implementation(libs.jedis)
    implementation(libs.amqp.client)

    implementation(platform(libs.google.cloud.bom))
    implementation(libs.google.api.client)
    implementation(libs.google.cloud.tasks)
    implementation(libs.google.cloud.scheduler)

    implementation(libs.firebase.admin)
    implementation(libs.librecur)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}

/**
 * Validates koin dependency injection at compile time
 */
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        archiveFileName.set("do-too-api.jar")
        mergeServiceFiles()
    }
}