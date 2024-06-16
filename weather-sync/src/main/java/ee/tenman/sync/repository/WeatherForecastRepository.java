package ee.tenman.sync.repository;

import ee.tenman.domain.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
	Optional<WeatherForecast> findByDateAndLocation(LocalDate date, String location);
}
