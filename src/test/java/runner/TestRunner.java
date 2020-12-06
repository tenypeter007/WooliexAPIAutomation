package runner;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty","html:target/Report"},
		glue = {"stepdefs"},
		features = {"src/test/features/Weatherbit.feature"})
public class TestRunner {
	
	
}
