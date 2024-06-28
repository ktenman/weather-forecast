plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("jacoco")
}

group = "ee.tenman"
version = "0.0.1-SNAPSHOT"
val springdocOpenApiVersion = "2.5.0"
val selenideVersion = "7.3.3"

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

extra["springCloudVersion"] = "2023.0.2"

dependencies {
    implementation(project(":weather-domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenApiVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation(project(":weather-db-migration"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.codeborne:selenide:$selenideVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

val isE2ETestEnvironmentEnabled = System.getenv("E2E")?.toBoolean() == true

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    if (isE2ETestEnvironmentEnabled) {
        configureE2ETestEnvironment()
    } else {
        exclude("**/e2e/**")
    }
    finalizedBy(":jacocoTestReport")
}

fun Test.configureE2ETestEnvironment() {
    include("**/e2e/**")
    val properties = mutableMapOf(
        "webdriver.chrome.logfile" to "build/reports/chromedriver.log",
        "webdriver.chrome.verboseLogging" to "true"
    )
    if (project.hasProperty("headless")) {
        properties["chromeoptions.args"] = "--headless,--no-sandbox,--disable-gpu"
    }
    systemProperties.putAll(properties)
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
