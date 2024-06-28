package ee.tenman.common.repository;

import ee.tenman.common.domain.Location;
import ee.tenman.common.domain.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
	@Query("SELECT wf FROM WeatherForecast wf JOIN wf.location l WHERE l.name ILIKE %:location% AND wf.date >= :date")
	List<WeatherForecast> findByLocationNameContainingIgnoreCaseAndDateGreaterThanEqual(
			@Param("location") String location,
			@Param("date") LocalDate date
	);
	
	Optional<WeatherForecast> findByDateAndLocation(LocalDate date, Location location);
}
