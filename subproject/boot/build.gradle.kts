repositories {
    mavenCentral()
}

dependencies {
    implementation(DOMAIN)
    implementation(INFRASTRUCTURE)
    implementation(PRESENTATION)
    implementation(LOGGING)

    implementation(TYPESAFE_CONFIG)

    testImplementation(testFixtures(PRESENTATION))
    testImplementation(testFixtures(INFRASTRUCTURE))

    testImplementation(KTOR_CLIENT_CORE)
    testImplementation(KTOR_CLIENT_CIO)
    testImplementation(KTOR_CLIENT_SERIALIZATION)
}
