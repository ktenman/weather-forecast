package ee.tenman.api;

import ee.tenman.domain.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(Constants.BASE_PACKAGE)
public class WeatherApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherApiApplication.class, args);
	}
	
}
