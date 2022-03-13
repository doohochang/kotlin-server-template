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
}
