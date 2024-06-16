package ee.tenman.sync.job;

import ee.tenman.domain.ForecastType;
import ee.tenman.domain.WeatherForecast;
import ee.tenman.domain.WeatherForecastDetails;
import ee.tenman.sync.external.WeatherForecastDto;
import ee.tenman.sync.external.WeatherServiceClient;
import ee.tenman.sync.repository.WeatherForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ee.tenman.domain.ForecastType.DAY;
import static ee.tenman.domain.ForecastType.NIGHT;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastRetrievalJob {
	
	private final WeatherServiceClient weatherServiceClient;
	private final WeatherForecastRepository weatherForecastRepository;
	
	@Scheduled(cron = "0 0/30 * * * *")
	public void runJob() {
		log.info("Retrieving weather forecasts");
		WeatherForecastDto weatherForecastDto = weatherServiceClient.getWeatherForecast();
		List<WeatherForecast> weatherForecasts = weatherForecastDto.getForecasts().stream()
				.flatMap(this::processForecast)
				.toList();
		weatherForecastRepository.saveAll(weatherForecasts);
		log.info("Retrieved {} weather forecasts", weatherForecasts.size());
	}
	
	private Stream<WeatherForecast> processForecast(WeatherForecastDto.ForecastDto forecastDto) {
		LocalDate date = forecastDto.getDate();
		
		Map<String, List<LocationWithForecast>> placesByLocation = Stream.concat(
				forecastDto.getDay().getPlaces().stream().map(place -> new LocationWithForecast(place, DAY)),
				forecastDto.getNight().getPlaces().stream().map(place -> new LocationWithForecast(place, NIGHT))
		).collect(groupingBy(LocationWithForecast::location, toList()));
		
		return placesByLocation.entrySet().stream()
				.map(entry -> getOrCreateWeatherForecast(entry, date));
	}
	
	private WeatherForecast getOrCreateWeatherForecast(
			Map.Entry<String, List<LocationWithForecast>> entry,
			LocalDate date
	) {
		String location = entry.getKey();
		WeatherForecast forecast = weatherForecastRepository
				.findByDateAndLocation(date, location)
				.orElseGet(() -> {
					WeatherForecast newForecast = new WeatherForecast();
					newForecast.setDate(date);
					newForecast.setLocation(location);
					return newForecast;
				});
		
		forecast.setDate(date);
		forecast.setLocation(location);
		
		entry.getValue().forEach(place -> {
			WeatherForecastDetails detail = new WeatherForecastDetails();
			detail.setPhenomenon(place.place().getPhenomenon());
			detail.setTemperatureMin(place.place().getTemperatureMin());
			detail.setTemperatureMax(place.place().getTemperatureMax());
			detail.setForecastType(place.forecastType());
			forecast.addWeatherForecastDetails(detail);
		});
		
		return forecast;
	}
	
	private record LocationWithForecast(WeatherForecastDto.PlaceDto place,
	                                    ForecastType forecastType) {
		String location() {
			return place.getLocation();
		}
	}
}
