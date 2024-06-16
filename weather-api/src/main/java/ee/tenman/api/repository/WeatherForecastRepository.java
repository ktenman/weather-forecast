package ee.tenman.api.repository;

import ee.tenman.domain.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
	Optional<WeatherForecast> findByDateAndLocation(LocalDate date, String location);
	
	@Query("SELECT wf FROM WeatherForecast wf WHERE wf.location ILIKE %:location% AND wf.date >= :today")
	List<WeatherForecast> findByLocationContainingIgnoreCaseAndDateAfterOrEqual(
			@Param("location") String location, @Param("today") LocalDate today);
}
