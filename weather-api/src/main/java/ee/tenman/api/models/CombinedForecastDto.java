package ee.tenman.api.models;

public record CombinedForecastDto(
		String date,
		String location,
		Double temperatureMin,
		Double temperatureMax
) {
}
