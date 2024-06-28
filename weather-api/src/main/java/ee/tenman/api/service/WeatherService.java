package ee.tenman.api.service;

import ee.tenman.common.domain.WeatherForecast;
import ee.tenman.common.repository.WeatherForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
	
	private final Clock clock;
	private final WeatherForecastRepository weatherForecastRepository;
	
	public List<WeatherForecast> getCombinedWeatherDetailsByLocationAndDateRange(String locationName) {
		LocalDate today = LocalDate.now(clock);
		return weatherForecastRepository.findByLocationNameContainingIgnoreCaseAndDateGreaterThanEqual(locationName, today);
	}
}
