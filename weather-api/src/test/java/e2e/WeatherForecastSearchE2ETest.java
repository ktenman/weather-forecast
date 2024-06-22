package e2e;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.id;

class WeatherForecastSearchE2ETest {
	
	private static final String DEFAULT_LOCATION = "Tartu";
	private static final String TALLINN = "Tallinn";
	
	@Test
	void shouldDisplayWeatherForecastForLocation_whenSearchingForValidLocation() {
		open("http://localhost:61234");
		
		$(id("location")).shouldNotHave(text(DEFAULT_LOCATION))
				.setValue(DEFAULT_LOCATION)
				.pressEnter();
		
		Selenide.sleep(1000);
		
		SelenideElement location = $(className("location"));
		if (location.exists()) {
			assertThat(location.text()).isEqualTo(DEFAULT_LOCATION);
		} else {
			fail("Could not find location");
		}
		
		assertThat($(By.id("location")).val()).isEqualTo(DEFAULT_LOCATION);
	}
	
	@Test
	void shouldNotDisplayWeatherForecastForLocation_whenSearchingForNonValidLocation() {
		open("http://localhost:61234");
		
		$(id("location")).shouldNotHave(text(TALLINN))
				.setValue(TALLINN)
				.pressEnter();
		
		Selenide.sleep(1000);
		
		SelenideElement location = $(className("location"));
		if (location.exists()) {
			assertThat(location.text()).isEqualTo(TALLINN);
			fail("Should not have found location");
		}
		
		assertThat($(By.id("location")).val()).isEqualTo(TALLINN);
		SelenideElement alertInfo = $(className("alert"));
		if (alertInfo.exists()) {
			assertThat(alertInfo.text()).isEqualTo("No weather forecast data found.");
		} else {
			fail("Could not find alert info");
		}
	}
	
}
