package ee.tenman.api.mapper;

import ee.tenman.api.models.CombinedForecastDto;
import ee.tenman.domain.ForecastType;
import ee.tenman.domain.WeatherForecast;
import ee.tenman.domain.WeatherForecastDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherMapperTest {
	
	private static Stream<Arguments> provideToCombinedForecastDtoArguments() {
		return Stream.of(
				Arguments.of(
						createWeatherForecast("2023-06-16", "London", 10.0, 20.0),
						"2023-06-16", "London", 10.0, 20.0
				),
				Arguments.of(
						createWeatherForecast("2023-06-17", "Paris", null, 25.0),
						"2023-06-17", "Paris", 25.0, 25.0
				)
		);
	}
	
	private static WeatherForecast createWeatherForecast(
			String date,
			String location,
			Double temperatureMin,
			Double temperatureMax
	) {
		WeatherForecast weatherForecast = new WeatherForecast();
		weatherForecast.setDate(LocalDate.parse(date));
		weatherForecast.setLocation(location);
		
		WeatherForecastDetails details = new WeatherForecastDetails();
		details.setTemperatureMin(temperatureMin);
		details.setTemperatureMax(temperatureMax);
		details.setForecastType(ForecastType.DAY);
		
		weatherForecast.addWeatherForecastDetails(details);
		
		return weatherForecast;
	}
	
	@Test
	void shouldGroupForecastsByLocation_whenToCombinedForecastDtoMapWithMultipleForecasts() {
		List<WeatherForecast> weatherForecasts = List.of(
				createWeatherForecast("2023-06-16", "London", 10.0, 20.0),
				createWeatherForecast("2023-06-16", "London", 12.0, 22.0),
				createWeatherForecast("2023-06-16", "Paris", 15.0, 25.0)
		);
		
		Map<String, List<CombinedForecastDto>> result = WeatherMapper.toCombinedForecastDtoMap(weatherForecasts);
		
		assertThat(result).hasSize(2)
				.containsKeys("London", "Paris");
		assertThat(result.get("London")).hasSize(2);
		assertThat(result.get("Paris")).hasSize(1);
	}
	
	@ParameterizedTest
	@MethodSource("provideToCombinedForecastDtoArguments")
	void shouldMapWeatherForecastToCombinedForecastDto_whenToCombinedForecastDtoWithVariousInputs(
			WeatherForecast weatherForecast,
			String expectedDate,
			String expectedLocation,
			Double expectedTemperatureMin,
			Double expectedTemperatureMax
	) {
		CombinedForecastDto result = WeatherMapper.toCombinedForecastDto(weatherForecast);
		
		assertThat(result.date()).isEqualTo(expectedDate);
		assertThat(result.location()).isEqualTo(expectedLocation);
		assertThat(result.temperatureMin()).isEqualTo(expectedTemperatureMin);
		assertThat(result.temperatureMax()).isEqualTo(expectedTemperatureMax);
	}
}
