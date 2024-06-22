plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("jacoco")
}

group = "ee.tenman"
version = "0.0.1-SNAPSHOT"
val springdocOpenApiVersion = "2.5.0"
val selenideVersion = "7.3.2"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-database-postgresql")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

val isE2ETestEnvironmentEnabled = System.getenv("E2E")?.toBoolean() == true

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    finalizedBy(":jacocoTestReport")
}

val skipJacoco: Boolean = false
val jacocoEnabled: Boolean = true
tasks.withType<JacocoReport> {
    isEnabled = jacocoEnabled
    if (skipJacoco) {
        enabled = false
    }
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
}
