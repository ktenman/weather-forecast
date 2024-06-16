package ee.tenman.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class WeatherForecast extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include
	private LocalDate date;
	@EqualsAndHashCode.Include
	private String location;
	
	@OneToMany(mappedBy = "weatherForecast", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<WeatherForecastDetails> weatherForecastDetails = new HashSet<>();
	
	public void addWeatherForecastDetails(WeatherForecastDetails weatherForecastDetails) {
		weatherForecastDetails.setWeatherForecast(this);
		this.weatherForecastDetails.add(weatherForecastDetails);
	}
	
}
