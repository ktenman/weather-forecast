package ee.tenman.sync.service;

import ee.tenman.common.domain.ProviderName;
import ee.tenman.common.domain.WeatherForecast;

import java.util.List;

public interface WeatherService {
	List<WeatherForecast> getWeatherForecasts();
	
	void saveAll(List<WeatherForecast> weatherForecasts);
	
	ProviderName getProviderName();
}
