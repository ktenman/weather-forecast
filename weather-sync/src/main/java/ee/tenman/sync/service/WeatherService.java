package ee.tenman.sync.service;

import ee.tenman.sync.external.WeatherForecastDto;

public interface WeatherService {
	WeatherForecastDto getWeatherForecast();
}
