package ee.tenman.api.configuration;

import ee.tenman.api.configuration.TimeUtility.CustomDuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUtilityTest {
	
	private static final long START_TIME_1 = 1000000000L;
	private static final long START_TIME_2 = 2000000000L;
	private static final long START_TIME_3 = 3000000000L;
	
	private static Stream<CustomDuration> provideCustomDurations() {
		return Stream.of(
				new CustomDuration(START_TIME_1),
				new CustomDuration(START_TIME_2),
				new CustomDuration(START_TIME_3)
		);
	}
	
	@Test
	void shouldReturnCustomDuration_whenDurationInSecondsIsCalled() {
		long startTime = System.nanoTime();
		
		CustomDuration customDuration = TimeUtility.durationInSeconds(startTime);
		
		assertThat(customDuration).isNotNull();
	}
	
	@ParameterizedTest
	@MethodSource("provideCustomDurations")
	void shouldReturnFormattedDuration_whenToStringIsCalled(CustomDuration customDuration) {
		String formattedDuration = customDuration.toString();
		
		assertThat(formattedDuration).matches("\\d+\\.\\d{3}");
	}
	
	@ParameterizedTest
	@MethodSource("provideCustomDurations")
	void shouldReturnFormattedDuration_whenAsStringIsCalled(CustomDuration customDuration) {
		String formattedDuration = customDuration.asString();
		
		assertThat(formattedDuration).matches("\\d+\\.\\d{3}");
	}
	
	@ParameterizedTest
	@MethodSource("provideCustomDurations")
	void shouldReturnNonEmptyString_whenAsStringIsCalled(CustomDuration customDuration) {
		String formattedDuration = customDuration.asString();
		
		assertThat(formattedDuration).isNotEmpty();
	}
}
