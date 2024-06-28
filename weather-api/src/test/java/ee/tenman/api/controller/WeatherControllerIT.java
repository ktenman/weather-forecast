package ee.tenman.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tenman.api.models.CombinedForecastDto;
import ee.tenman.domain.ForecastType;
import ee.tenman.domain.Location;
import ee.tenman.domain.WeatherForecast;
import ee.tenman.domain.WeatherForecastDetails;
import ee.tenman.domain.integrationtest.IntegrationTest;
import ee.tenman.domain.repository.LocationRepository;
import ee.tenman.domain.repository.WeatherForecastRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ExtendWith(OutputCaptureExtension.class)
class WeatherControllerIT {
	
	@Resource
	private WeatherForecastRepository weatherForecastRepository;
	
	@Resource
	private LocationRepository locationRepository;
	
	@Resource
	private ObjectMapper objectMapper;
	
	@Resource
	private MockMvc mockMvc;
	
	@MockBean
	private Clock clock;
	
	@Test
	void shouldReturnWeatherForecastForLocation_whenGetWeatherForecastWithValidLocation(CapturedOutput capturedOutput) throws Exception {
		insertWeatherForecastToDB();
		when(clock.instant()).thenReturn(Instant.parse("2024-06-16T12:15:00Z"));
		when(clock.getZone()).thenReturn(UTC);
		String location = "Tartu";
		
		String responseJson = mockMvc.perform(get("/api/weather/forecast")
						.param("location", location)
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Map<String, List<CombinedForecastDto>> response = objectMapper.readValue(responseJson,
				new TypeReference<>() {
				});
		assertThat(response).hasSize(1).containsKey(location);
		assertThat(response.get(location)).singleElement().satisfies(
				forecast -> {
					assertThat(forecast.date()).isEqualTo("2024-06-16");
					assertThat(forecast.location()).isEqualTo(location);
					assertThat(forecast.temperatureMin()).isEqualTo(8.1);
					assertThat(forecast.temperatureMax()).isEqualTo(12.5);
				}
		);
		assertThat(capturedOutput).as("Checking if @Loggable annotation is used")
				.contains("WeatherController.getWeatherForecast(..) entered with arguments: [\"Tartu\"]")
				.contains("WeatherController.getWeatherForecast(..) exited with result: {\"Tartu\":[{\"date\":\"2024-06-16\",\"location\":\"Tartu\",\"temperatureMin\":8.1,\"temperatureMax\":12.5}]}");
	}
	
	private void insertWeatherForecastToDB() {
		WeatherForecast weatherForecast = new WeatherForecast();
		weatherForecast.setDate(LocalDate.parse("2024-06-16"));
		Location location = new Location();
		location.setName("Tartu");
		weatherForecast.setLocation(locationRepository.saveAndFlush(location));
		WeatherForecastDetails details = new WeatherForecastDetails();
		details.setForecastType(ForecastType.DAY);
		details.setTemperatureMin(8.1);
		details.setTemperatureMax(12.5);
		details.setPhenomenon("Few clouds");
		weatherForecast.addWeatherForecastDetails(details);
		weatherForecastRepository.saveAndFlush(weatherForecast);
	}
}
