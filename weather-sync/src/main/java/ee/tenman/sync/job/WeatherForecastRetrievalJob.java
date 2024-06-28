package ee.tenman.sync.job;

import ee.tenman.common.domain.ForecastType;
import ee.tenman.common.domain.Location;
import ee.tenman.common.domain.WeatherForecast;
import ee.tenman.common.domain.WeatherForecastDetails;
import ee.tenman.common.repository.WeatherForecastRepository;
import ee.tenman.sync.external.WeatherForecastDto;
import ee.tenman.sync.external.WeatherForecastDto.PlaceDto;
import ee.tenman.sync.service.LocationService;
import ee.tenman.sync.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ee.tenman.common.domain.ForecastType.DAY;
import static ee.tenman.common.domain.ForecastType.NIGHT;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastRetrievalJob {
	
	private final WeatherService weatherService;
	private final WeatherForecastRepository weatherForecastRepository;
	private final LocationService locationService;
	
	@Scheduled(cron = "0 0/30 * * * *")
	public void runJob() {
		log.info("Retrieving weather forecasts");
		WeatherForecastDto weatherForecastDto = weatherService.getWeatherForecast();
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
		Location location = locationService.getOrCreateLocation(entry.getKey());
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
	
	private record LocationWithForecast(
			PlaceDto place,
			ForecastType forecastType
	) {
		String location() {
			return place.getLocation();
		}
	}
}
