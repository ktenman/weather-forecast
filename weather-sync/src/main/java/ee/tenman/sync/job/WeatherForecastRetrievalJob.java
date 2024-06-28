package ee.tenman.sync.job;

import ee.tenman.common.domain.WeatherForecast;
import ee.tenman.sync.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastRetrievalJob {
	
	private final WeatherService weatherService;
	
	@Scheduled(cron = "0 0/30 * * * *")
	public void runJob() {
		log.info("Retrieving weather forecasts");
		List<WeatherForecast> weatherForecasts = weatherService.getWeatherForecasts();
		weatherService.saveAll(weatherForecasts);
		log.info("Retrieved {} weather forecasts", weatherForecasts.size());
	}
	
}
