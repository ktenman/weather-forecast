# Weather Forecast

[![Build & Test](https://github.com/ktenman/weather-forecast/actions/workflows/ci.yml/badge.svg)](https://github.com/ktenman/weather-forecast/actions/workflows/ci.yml)

## Introduction

The Weather Forecast application retrieves weather data from the Ilmateenistus API, stores it in a database, and provides a user-friendly interface for viewing weather forecasts for various locations in Estonia.

## Prerequisites

Before you begin, ensure your system meets the following requirements:

- Java: v21.0.2
- Gradle: v8.5
- Node.js: v20.11.1
- npm: v10.2.4
- Docker: v25.0.2
- Docker Compose: v2.24.3

## Technical Stack

- **Backend**: Spring Boot v3.3
- **Frontend**:
    - **Build Tool**: Vite
    - Vue.js v3.4
    - Bootstrap v5.3
- **Database**: PostgreSQL for data persistence and Flyway for database migration management
- **Caching**: Redis, utilized for caching weather data
- **Testing**: JUnit, Mockito, AssertJ, and Testcontainers for robust testing coverage


## Architecture

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

LAYOUT_WITH_LEGEND()

Person(user, "User", "End user of the Weather Forecast application")

Container(frontend, "Frontend", "Vue.js, Bootstrap", "Provides the user interface for viewing weather forecasts")
Container(backend, "Backend", "Spring Boot, Java", "Handles API requests, data processing, and database interactions")
Container(database, "Database", "PostgreSQL", "Stores weather forecast data")
Container(cache, "Cache", "Redis", "Caches weather forecast data for improved performance")
Container(sync, "Sync", "Spring Boot, Java", "Retrieves weather data from the Ilmateenistus API on a scheduled basis")

System_Ext(ilmateenistus, "Ilmateenistus API", "Provides weather forecast data")

Rel(user, frontend, "Uses", "HTTP")
Rel(frontend, backend, "Sends requests to", "HTTP")
Rel(backend, database, "Reads from and writes to", "JDBC")
Rel(backend, cache, "Reads from and writes to", "Redis protocol")
Rel(sync, ilmateenistus, "Retrieves weather data from", "HTTP")
Rel(sync, database, "Writes weather data to", "JDBC")
@enduml
```

## Setup and Running Instructions
### Docker Containers Setup
Initialize necessary Docker containers with Docker Compose to ensure the database and Redis services are up before proceeding:
```
docker-compose -f compose.yml up -d
```
### Backend Setup
Navigate to the root directory and compile the Java application using Gradle:
```
cd weather-api 
../gradlew clean build
../gradlew bootRun
```
### Frontend Setup
Navigate to the `ui` directory, install frontend dependencies, and start the development server:
```
cd ui
npm install
npm run dev
```

### Running in Production
To run the application in production, use Docker Compose:
```
docker-compose -f docker-compose.yml up -d
```
### Updating the Application
To update the application or its services after making changes:
  1. Rebuild the services:
  ```
  docker-compose -f docker-compose.yml build
  ```
  2. Restart the services for the changes to take effect:
  ```
  docker-compose -f docker-compose.yml up -d
  ```

### End-to-End Tests on Unix System
  1. Start Docker Containers:
  ```
  docker-compose -f docker-compose.yml -f docker-compose.e2e.yml up -d
  ```
  2. Run E2E Tests:
  ```
  cd weather-api 
  export E2E=true
  ../gradlew test --info -Pheadless=true
  ```
### Continuous Integration and Deployment
* A CI pipeline via GitHub Actions in the `.github` folder automates unit and integration tests.
* Dependabot keeps Maven and GitHub Actions versions up-to-date, automating dependency management.

### Key Features
* Weather Forecast Retrieval: Retrieves weather forecast data from the Ilmateenistus API.
* Data Persistence: Stores weather forecast data in a PostgreSQL database.
* Caching: Caches weather forecast data using Redis for improved performance.
* User Interface: Provides a user-friendly interface for viewing weather forecasts for various locations in Estonia.

---
This README aims to guide developers through setting up, running, and understanding the core functionalities and technical aspects of the Weather Forecast application.
