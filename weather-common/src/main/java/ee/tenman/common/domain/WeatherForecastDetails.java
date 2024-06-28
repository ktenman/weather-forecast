package ee.tenman.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class WeatherForecastDetails extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "forecast_id")
	@EqualsAndHashCode.Include
	private WeatherForecast weatherForecast;
	
	private Double temperatureMin;
	private Double temperatureMax;
	private String phenomenon;
	@Enumerated(EnumType.STRING)
	@EqualsAndHashCode.Include
	private ForecastType forecastType;
}
