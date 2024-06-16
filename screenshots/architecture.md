```
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
