plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("jacoco")
}

group = "ee.tenman"
version = "0.0.1-SNAPSHOT"

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

repositories {
    mavenCentral()
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
