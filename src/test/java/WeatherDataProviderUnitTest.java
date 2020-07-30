import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherDataProviderUnitTest {



    @Test
    public void testValidRequest(){
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        HashMap<String, String> output = weatherDataProviderObject.getWeatherData();
        assertEquals(output.get("responseStatus")+"", "200");
    }


    @Test
    public void testInvalidRequestWithInvalidLatitudeLongitude(){
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        HashMap<String, String> output = weatherDataProviderObject.getWeatherData("invalid latitude","invalid longitude");
        assertNotEquals(output.get("responseStatus")+"", "200");
    }

    @Test
    public void testInvalidRequestWithInvalidKey(){
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        WeatherDataProvider.setApiKey("invalid key");
        HashMap<String, String> output = weatherDataProviderObject.getWeatherData();
        assertNotEquals(output.get("responseStatus")+"", "200");
    }

    @Test
    public void testDataIsNotNullOnSuccess(){
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        HashMap<String, String> output = weatherDataProviderObject.getWeatherData();
        assertNotNull(output.get("currentWeatherSummary"));
        assertNotNull(output.get("todayWeatherSummary"));
        assertNotNull(output.get("todayPrecipProbability"));
    }

    @Test (expected = InvalidParameterException.class)
    public void testInvalidCityName(){
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        HashMap<String, String> output = weatherDataProviderObject.getWeatherData("Invalid city"); //Invalid city name exception
    }



}
