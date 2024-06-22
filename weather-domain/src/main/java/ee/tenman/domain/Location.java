package ee.tenman.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Location extends BaseEntity {
	private String name;
	
	@OneToMany(mappedBy = "location")
	private List<WeatherForecast> weatherForecasts = new ArrayList<>();
}
