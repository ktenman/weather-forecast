package ee.tenman.sync;

import ee.tenman.domain.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EntityScan(Constants.BASE_PACKAGE)
public class WeatherSyncApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherSyncApplication.class, args);
	}
	
}
