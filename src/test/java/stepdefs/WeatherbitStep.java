package stepdefs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import objects.BaseHolder;

public class WeatherbitStep {

	BaseHolder jsonPlaceHolder = new BaseHolder();
	private Scenario scenario;

	@Before
	public void beforeTest(Scenario scenario) {
		this.scenario = scenario;
	}

	@When("^I like to surf in any 2 beaches Out of top ten of Sydney$")
	public void iGetAListOfBlogPostsUsingTheAPIEndpoint() throws IOException {
		jsonPlaceHolder.readFromCSV();

	}

	@And("^I only like to surf on any 2 days specifically <Thursday & Friday> in next 16 Days$")
	public void selectDate() {

	}

	@When("^I look up the the weather forecast for the next 16 days using POSTAL CODES$")
	public void lookForWeather() {
		HashMap<String, String> map = jsonPlaceHolder.getMap();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			org.junit.Assert.assertTrue(jsonPlaceHolder.getWeatherBasedOnPostcode(key, scenario));
		}
	}

	@Then("^I check to if see the temperature is between \"(.*)\" ℃ and \"(.*)\" ℃ and UV index is <= \"(.*)\"$")
	public void checkTemp(float min_tem, float max_temp, float uv) {
		jsonPlaceHolder.checkTemp(min_tem,max_temp,uv);
	}

	@And("^I Pick two spots based on suitable weather forecast for the day$")
	public void getIdealPlace() {
		
		if (jsonPlaceHolder.getIdealPlaces() != null && jsonPlaceHolder.getIdealPlaces().isEmpty() == false)
		{
			for (String places : jsonPlaceHolder.getIdealPlaces()) {
				System.out.println(places);
				scenario.write(places);
			}
		}

		else
			scenario.write("No Places to Visit");
	}

}
