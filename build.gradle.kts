plugins {
    kotlin("jvm") version "2.1.20-Beta1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "codes.towel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("com.github.NEZNAMY", "TAB-API", "5.2.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
