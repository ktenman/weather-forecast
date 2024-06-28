package ee.tenman.sync.service;

import ee.tenman.sync.external.WeatherForecastDto;
import ee.tenman.sync.external.WeatherServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IlmateenistusWeatherService implements WeatherService {
	
	private final WeatherServiceClient weatherServiceClient;
	
	@Override
	public WeatherForecastDto getWeatherForecast() {
		return weatherServiceClient.getWeatherForecast();
	}
}
