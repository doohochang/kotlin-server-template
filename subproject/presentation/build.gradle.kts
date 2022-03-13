plugins {
    kotlin("plugin.serialization") version Version.KOTLIN
}

dependencies {
    implementation(DOMAIN)
    implementation(LOGGING)

    implementation(KOTLINX_SERIALIZATION_JSON)

    implementation(KTOR_SERVER_CORE)
    implementation(KTOR_SERVER_NETTY)
    implementation(KTOR_SERIALIZATION)

    implementation(TYPESAFE_CONFIG)

    testImplementation(testFixtures(LOGGING))

    testFixturesImplementation(KTOR_CLIENT_CORE)
    testFixturesImplementation(KTOR_CLIENT_CIO)
}
