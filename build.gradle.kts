val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikaricp_version: String by project
val junit_version: String by project
val mockk_version: String by project
val koin_version: String by project
val jbcrypt_version: String by project
val h2_version: String by project
val kotlin_coroutines_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
}

group = "org.codingforanimals"
version = "0.0.1"
application {
    mainClass.set("org.codingforanimals.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    // Logger
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")

    // BCrypt
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")

    // Database
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")

    // ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")

    // H2 for testing only
    testImplementation("com.h2database:h2:$h2_version")

    // Coroutines for testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlin_coroutines_version")

}