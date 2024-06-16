package ee.tenman.migration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WeatherDBMigrationApplication {
	
	public static void main(String[] args) {
		log.info("Starting WeatherDBMigrationApplication");
		SpringApplication.run(WeatherDBMigrationApplication.class, args);
		log.info("Migrated database to the latest version");
		System.exit(0);
	}
	
}
