package ee.tenman.sync.external.ilmateenistus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDto {
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "forecast")
	private List<ForecastDto> forecasts;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ForecastDto {
		@JsonProperty("date")
		private LocalDate date;
		
		@JacksonXmlProperty(localName = "night")
		private NightDayDto night;
		
		@JacksonXmlProperty(localName = "day")
		private NightDayDto day;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NightDayDto {
		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(localName = "place")
		private List<PlaceDto> places = new ArrayList<>();
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PlaceDto {
		@JacksonXmlProperty(localName = "name")
		private String location;
		
		@JacksonXmlProperty(localName = "phenomenon")
		private String phenomenon;
		
		@JacksonXmlProperty(localName = "tempmin")
		private Double temperatureMin;
		
		@JacksonXmlProperty(localName = "tempmax")
		private Double temperatureMax;
	}
}
