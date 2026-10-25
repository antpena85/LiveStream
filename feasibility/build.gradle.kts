plugins {
    kotlin("jvm") version "2.4.21"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.53.4.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.4.4")
}

tasks.test {
    useJUnit()
    systemProperty("feasibility.runtimeClasspath", sourceSets["main"].runtimeClasspath.asPath)
    testLogging {
        events("passed", "failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
