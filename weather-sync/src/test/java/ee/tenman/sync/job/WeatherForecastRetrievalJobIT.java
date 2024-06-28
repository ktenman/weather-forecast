package ee.tenman.sync.job;

import ee.tenman.common.domain.ForecastType;
import ee.tenman.common.domain.Location;
import ee.tenman.common.domain.ProviderName;
import ee.tenman.common.domain.WeatherForecast;
import ee.tenman.common.integrationtest.IntegrationTest;
import ee.tenman.common.repository.WeatherForecastRepository;
import jakarta.annotation.Resource;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@IntegrationTest
@AutoConfigureWireMock(port = 0)
class WeatherForecastRetrievalJobIT {
	private static final String FORECAST_URL = ".*/ilma_andmed/xml/forecast.php.*";
	
	@Resource
	private WeatherForecastRetrievalJob weatherForecastRetrievalJob;
	
	@Resource
	private WeatherForecastRepository weatherForecastRepository;
	
	@Test
	void shouldNotCreateDuplicatedRows_whenTriggeredMultipleTimesWithSameData() {
		stubForForecastXml("forecast_2024-06-16.xml");
		
		weatherForecastRetrievalJob.runJob();
		weatherForecastRetrievalJob.runJob();
		
		List<WeatherForecast> allForecasts = weatherForecastRepository.findAll();
		assertThat(allForecasts).isNotEmpty().hasSize(6).first().satisfies(weatherForecast -> {
			assertThat(weatherForecast.getLocation().getName()).isEqualTo("Jõhvi");
			assertThat(weatherForecast.getProviderName()).isEqualTo(ProviderName.ILMATEENISTUS);
			assertThat(weatherForecast.getDate()).isEqualTo("2024-06-16");
			assertThat(weatherForecast.getWeatherForecastDetails()).hasSize(2)
					.anySatisfy(details -> {
						assertThat(details.getForecastType()).isEqualTo(ForecastType.NIGHT);
						assertThat(details.getTemperatureMin()).isEqualTo(8);
						assertThat(details.getTemperatureMax()).isNull();
						assertThat(details.getPhenomenon()).isEqualTo("Few clouds");
					}).anySatisfy(details -> {
						assertThat(details.getForecastType()).isEqualTo(ForecastType.DAY);
						assertThat(details.getTemperatureMin()).isNull();
						assertThat(details.getTemperatureMax()).isEqualTo(22);
						assertThat(details.getPhenomenon()).isEqualTo("Variable clouds");
					});
		});
		assertThat(allForecasts).extracting(WeatherForecast::getLocation).extracting(Location::getName)
				.containsOnly("Jõhvi", "Kuressaare", "Pärnu", "Türi", "Harku", "Tartu");
	}
	
	@Test
	void shouldSaveNewData_whenTriggeredWithNewDataAfterInitialRun() {
		stubForForecastXml("forecast_2024-06-16.xml");
		weatherForecastRetrievalJob.runJob();
		
		stubForForecastXml("forecast_2024-06-17.xml");
		weatherForecastRetrievalJob.runJob();
		
		List<WeatherForecast> allForecasts = weatherForecastRepository.findAll();
		assertThat(allForecasts).hasSize(12)
				.extracting(wf -> wf.getLocation().getName(), f -> f.getDate().toString())
				.contains(
						Tuple.tuple("Jõhvi", "2024-06-16"),
						Tuple.tuple("Kuressaare", "2024-06-16"),
						Tuple.tuple("Pärnu", "2024-06-16"),
						Tuple.tuple("Türi", "2024-06-16"),
						Tuple.tuple("Harku", "2024-06-16"),
						Tuple.tuple("Tartu", "2024-06-16"),
						Tuple.tuple("Jõhvi", "2024-06-17"),
						Tuple.tuple("Kuressaare", "2024-06-17"),
						Tuple.tuple("Pärnu", "2024-06-17"),
						Tuple.tuple("Türi", "2024-06-17"),
						Tuple.tuple("Harku", "2024-06-17"),
						Tuple.tuple("Tartu", "2024-06-17")
				);
	}
	
	private void stubForForecastXml(String filename) {
		stubFor(get(urlPathMatching(FORECAST_URL))
				.willReturn(aResponse()
						.withHeader(CONTENT_TYPE, APPLICATION_XML_VALUE)
						.withBodyFile(filename)));
	}
	
}
