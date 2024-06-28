package ee.tenman.sync.service;

import ee.tenman.common.domain.ForecastType;
import ee.tenman.common.domain.Location;
import ee.tenman.common.domain.WeatherForecast;
import ee.tenman.common.domain.WeatherForecastDetails;
import ee.tenman.common.repository.WeatherForecastRepository;
import ee.tenman.sync.external.ilmateenistus.WeatherForecastDto;
import ee.tenman.sync.external.ilmateenistus.WeatherForecastDto.ForecastDto;
import ee.tenman.sync.external.ilmateenistus.WeatherServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ee.tenman.common.domain.ForecastType.DAY;
import static ee.tenman.common.domain.ForecastType.NIGHT;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class IlmateenistusWeatherService implements WeatherService {
	
	private final WeatherForecastRepository weatherForecastRepository;
	private final WeatherServiceClient weatherServiceClient;
	private final LocationService locationService;
	
	@Override
	public List<WeatherForecast> getWeatherForecasts() {
		WeatherForecastDto weatherForecastDto = weatherServiceClient.getWeatherForecast();
		return weatherForecastDto.getForecasts().stream()
				.flatMap(this::processForecast)
				.toList();
	}
	
	@Override
	public void saveAll(List<WeatherForecast> weatherForecasts) {
		weatherForecastRepository.saveAllAndFlush(weatherForecasts);
	}
	
	private Stream<WeatherForecast> processForecast(ForecastDto forecastDto) {
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
			WeatherForecastDto.PlaceDto place,
			ForecastType forecastType
	) {
		String location() {
			return place.getLocation();
		}
	}
}
