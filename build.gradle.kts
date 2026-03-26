plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("io.github.wimdeblauwe:htmx-spring-boot:5.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb-test")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.18.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Exec>("buildDockerImage") {
    group = "docker"
    description = "Builds and pushes the Docker image"
    // Check if docker is available before running the command
    doFirst {
        val process = ProcessBuilder("which", "docker").start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw GradleException("Docker is not installed or not in PATH. Please install Docker and ensure it is available in your PATH.")
        }
    }
    commandLine("which", "docker")
    commandLine("docker", "version")
//    commandLine(
//        "docker", "buildx", "build",
//        "--no-cache",
//        "--push",
//        "--platform", "linux/amd64",
//        "--tag", "jonitooo/flexo:1.0.0",
//        "."
//    )
}
