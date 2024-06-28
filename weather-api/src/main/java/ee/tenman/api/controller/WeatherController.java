package ee.tenman.api.controller;

import ee.tenman.api.configuration.logging.aspect.Loggable;
import ee.tenman.api.mapper.WeatherMapper;
import ee.tenman.api.models.CombinedForecastDto;
import ee.tenman.api.service.WeatherService;
import ee.tenman.common.domain.WeatherForecast;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
	
	private final WeatherService weatherService;
	
	@Loggable
	@GetMapping(value = "/forecast", produces = APPLICATION_JSON_VALUE)
	public Map<String, List<CombinedForecastDto>> getWeatherForecast(
			@RequestParam(value = "location", defaultValue = "") String location
	) {
		List<WeatherForecast> weatherForecasts = weatherService.getCombinedWeatherDetailsByLocationAndDateRange(location);
		return WeatherMapper.toCombinedForecastDtoMap(weatherForecasts);
	}
}
