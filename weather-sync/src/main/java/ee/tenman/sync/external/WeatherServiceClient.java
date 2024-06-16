package ee.tenman.sync.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@FeignClient(name = "weather-service", url = "${ilmateenistus.url}")
public interface WeatherServiceClient {
	@GetMapping(value = "/ilma_andmed/xml/forecast.php?lang=eng", consumes = APPLICATION_XML_VALUE)
	WeatherForecastDto getWeatherForecast();
}
