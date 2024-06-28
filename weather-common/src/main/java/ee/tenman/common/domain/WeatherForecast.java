package ee.tenman.common.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class WeatherForecast extends BaseEntity {
	@EqualsAndHashCode.Include
	private LocalDate date;
	@EqualsAndHashCode.Include
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	
	@OneToMany(mappedBy = "weatherForecast", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<WeatherForecastDetails> weatherForecastDetails = new HashSet<>();
	
	public void addWeatherForecastDetails(WeatherForecastDetails weatherForecastDetails) {
		weatherForecastDetails.setWeatherForecast(this);
		this.weatherForecastDetails.add(weatherForecastDetails);
	}
	
}
