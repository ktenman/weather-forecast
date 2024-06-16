package ee.tenman.api.mapper;

import ee.tenman.api.models.CombinedForecastDto;
import ee.tenman.domain.WeatherForecast;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherMapper {
	
	public static Map<String, List<CombinedForecastDto>> toCombinedForecastDtoMap(List<WeatherForecast> weatherForecasts) {
		return weatherForecasts.stream()
				.map(WeatherMapper::toCombinedForecastDto)
				.collect(groupingBy(CombinedForecastDto::location));
	}
	
	public static CombinedForecastDto toCombinedForecastDto(WeatherForecast weatherForecast) {
		List<Double> temperatures = weatherForecast.getWeatherForecastDetails().stream()
				.flatMap(details -> Stream.of(details.getTemperatureMin(), details.getTemperatureMax()))
				.filter(Objects::nonNull)
				.toList();
		
		Double temperatureMin = temperatures.stream()
				.mapToDouble(Double::doubleValue)
				.min()
				.orElse(Double.NaN);
		Double temperatureMax = temperatures.stream()
				.mapToDouble(Double::doubleValue)
				.max()
				.orElse(Double.NaN);
		
		return new CombinedForecastDto(
				weatherForecast.getDate().toString(),
				weatherForecast.getLocation(),
				temperatureMin,
				temperatureMax
		);
	}
}
