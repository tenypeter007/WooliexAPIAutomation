package objects;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jayway.restassured.path.json.JsonPath;

import cucumber.api.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseHolder {
	private Response response;
	private RequestSpecification request;
	private Scenario scenario;
	HashMap<String, String> map = new HashMap<String, String>();

	// Initializing variables

	private String ENDPOINT = "https://api.weatherbit.io/v2.0/forecast/daily?key=030b3388c3e94261bda2ac5d4109f6ac";
	HashMap<String, String> reponsePostcode = new HashMap<String, String>();
	List<String> idealPlaces = new ArrayList<String>();

	/**
	 * @desc read from test data csv(top ten beaches with postcode) and convert to
	 *       Hashmap
	 * @param none
	 * @return none
	 */

	public void readFromCSV() throws IOException {

		// Get file from resoureces

		String fileName = "Testdata/postcodes.csv";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		// Read CSV
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;

		while ((line = br.readLine()) != null) {

			String str[] = line.split(",");
			for (int i = 0; i < str.length; i++) {
				String arr[] = str[i].split(";");
				map.put(arr[0], arr[1]);
			}
		}

	}

	/**
	 * @desc Retruns post code Hashmap
	 * @param none
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getMap() {
		return map;
	}

	/**
	 * @desc Call weatherbit Get Api with postcode
	 * @param postcode , scenario
	 * @return boolean
	 */
	public boolean getWeatherBasedOnPostcode(String postcode, Scenario scenario) {
		// Initializing variables
		this.scenario = scenario;
		boolean flag = false;
		request = given().queryParam("postal_code", postcode);
		response = request.when().get(ENDPOINT);
		reponsePostcode.put(postcode, response.asString());
		JsonParser parser = new JsonParser();
		scenario.write("RESPONSE for post code:" + postcode + response.asString());
		// Validating json response
		try {
			parser.parse(response.asString());
			flag = true;
		} catch (JsonSyntaxException jse) {
			System.out.println("Not a valid Json String:" + jse.getMessage());
			flag = false;
		}
		// Returns boolean flag
		return flag;
	}

	/**
	 * @desc Get places where places with temp between 20℃ and 30℃ and uv >=3
	 * @param minimum Temperature, maximum Temperature, UV 
	 * @return none
	 */
	public void checkTemp(float min_tem,float max_temp,float UV) {

		Iterator it = reponsePostcode.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			JsonPath jsonPath = new JsonPath((String) pair.getValue());
			// Getting data from response
			List<HashMap<String, Object>> data = jsonPath.getJsonObject("data");

			for (HashMap<String, Object> s : data) {
				int temperature;
				int uv;
				if (s.get("temp").getClass() == Float.class)
					temperature = Math.round((float) s.get("temp"));
				else
					temperature = (int) s.get("temp");

				if (s.get("uv").getClass() == Float.class)
					uv = Math.round((float) s.get("uv"));
				else
					uv = (int) s.get("uv");
				if (temperature > min_tem && temperature < max_temp && uv <= UV) {

					long date_time = (int) s.get("ts");
					Date date = new Date(date_time * 1000);
					SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat jdf2 = new SimpleDateFormat("EE");
					String fullDate = jdf.format(date);
					String dayOfTheWeek = jdf2.format(date);
					if (dayOfTheWeek.trim().equals("Thu") || dayOfTheWeek.trim().equals("Fri")) {
						idealPlaces.add("You can vist " + map.get(pair.getKey()) + " on " + fullDate);
					}
				}

			}
			it.remove();
		}

	}

	/**
	 * @desc Get places matching the criteria
	 * @param none
	 * @return List<String>
	 */

	public List<String> getIdealPlaces() {
		return idealPlaces;
	}
}
