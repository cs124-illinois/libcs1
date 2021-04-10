import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.cs125-illinois.libcs1"
version = "2021.4.0"

plugins {
    kotlin("jvm") version "1.4.32"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.4.0"
    id("com.github.ben-manes.versions") version "0.38.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
}
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    implementation(kotlin("stdlib"))
}
tasks.withType<KotlinCompile> {
    val javaVersion = JavaVersion.VERSION_1_8.toString()
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    kotlinOptions {
        jvmTarget = javaVersion
    }
}
tasks.dependencyUpdates {
    resolutionStrategy {
        componentSelection {
            all {
                if (listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea", "eap", "pr").any { qualifier ->
                        candidate.version.matches(Regex("(?i).*[.-]$qualifier[.\\d-+]*"))
                    }) {
                    reject("Release candidate")
                }
            }
        }
    }
    gradleReleaseChannel = "current"
}
publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["java"])
        }
    }
}
detekt {
    buildUponDefaultConfig = true
}
