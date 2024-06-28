package ee.tenman.sync;

import ee.tenman.common.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
@EnableScheduling
@EntityScan(Constants.BASE_PACKAGE)
@EnableJpaRepositories(basePackages = Constants.REPOSITORY_BASE_PACKAGE)
public class WeatherSyncApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherSyncApplication.class, args);
	}
	
}
