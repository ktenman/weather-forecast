package ee.tenman.sync.service;

import ee.tenman.sync.job.WeatherForecastRetrievalJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class OnceForecastRetrievalService {
	
	private final WeatherForecastRetrievalJob weatherForecastRetrievalJob;
	
	@PostConstruct
	public void init() {
		log.info("Starting once forecast retrieval");
		weatherForecastRetrievalJob.runJob();
	}
}
