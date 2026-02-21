plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "de.loewenjunges.hytale"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Hytale release repository
    maven("https://maven.hytale.com/release") // https://maven.hytale.com/release/com/hypixel/hytale/Server/maven-metadata.xml

    // Hytale pre-release repository
    maven("https://maven.hytale.com/pre-release") // https://maven.hytale.com/pre-release/com/hypixel/hytale/Server/maven-metadata.xml
}

dependencies {
    compileOnly("com.hypixel.hytale:Server:2026.02.19-1a311a592")

    compileOnly("org.jetbrains:annotations:26.0.2-1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")

//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}