package ee.tenman.sync.external.ilmateenistus;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@FeignClient(name = "ilmateenistus-api", url = "${ilmateenistus.url}")
public interface IlmateenistusApiClient {
	@GetMapping(value = "/ilma_andmed/xml/forecast.php?lang=eng", consumes = APPLICATION_XML_VALUE)
	@Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
	WeatherForecastDto getWeatherForecast();
}
