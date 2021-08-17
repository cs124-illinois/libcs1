group = "com.github.cs125-illinois"
version = "2021.8.0"

plugins {
    kotlin("jvm") version "1.5.21"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.5.0"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
}
repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}
dependencies {
    implementation(kotlin("stdlib"))
}
tasks.dependencyUpdates {
    fun String.isNonStable() = !(
        listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
            || "^[0-9,.v-]+(-r)?$".toRegex().matches(this)
        )
    rejectVersionIf { candidate.version.isNonStable() }
    gradleReleaseChannel = "current"
}
publishing {
    publications {
        create<MavenPublication>("libcs1") {
            from(components["java"])
        }
    }
}
detekt {
    buildUponDefaultConfig = true
}
