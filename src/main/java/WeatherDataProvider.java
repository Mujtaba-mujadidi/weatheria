/**
 * Allows user to query weather forecast information for a default location, given city name, or
 * given latitude and longitude.
 */

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class WeatherDataProvider {

    private static String COORDINATES_URL;
    private static String API_KEY;
    private String  apiUrl;
    private HashMap<String, Coordinate> locationsToCoordinatesMap;

    public WeatherDataProvider(){
        COORDINATES_URL = "https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json";
        API_KEY = "1a43d545710c14571dbbe87b13bad8c7";
        locationsToCoordinatesMap = new HashMap<>();
        this.requestCoordinatesData();
    }

    /**
     * @return weather forecast for the coordinates (latitude: 60.59329987, longitude: -1.44250533).
     */
    public  HashMap<String, String>  getWeatherData() {
        return this.requestWeatherData("60.59329987", "-1.44250533");
    }

    /**
     * @param latitude
     * @param longitude
     * @return weather forecast for the given latitude and longitude.
     */
    public  HashMap<String, String>  getWeatherData(String latitude, String longitude){
        return this.requestWeatherData(latitude, longitude);
    }

    /**
     * @param cityName
     * @return weather forecast for the given city name.
     * @throws Exception Exception is thrown if the city coordinates is not found.
     */
    public  HashMap<String, String> getWeatherData(String cityName) {
        Coordinate coordinate = this.locationsToCoordinatesMap.get(cityName.toLowerCase());
        if(coordinate!=null){
            return this.requestWeatherData(coordinate.getLatitude(), coordinate.getLongitude());
        }else{
            throw new InvalidParameterException("No results found for "+cityName);
        }
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @return Weather for cast for the given latitude and logitude.
     */
    private HashMap<String, String> requestWeatherData(String latitude, String longitude){
        HashMap toReturn = new HashMap();

        this.apiUrl = this.getAPIURL(latitude, longitude);

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(this.apiUrl).asJson();

            //On successful request, get the data from the json response.
            if(jsonResponse.getStatus() == 200){
                for(Object responseObject: jsonResponse.getBody().getArray()){
                    JSONObject responseData = (JSONObject) responseObject;

                    //A data point containing the current weather conditions at the requested location.
                    JSONObject currentWeatherData = responseData.getJSONObject("currently");

                    //A data block containing the weather conditions day-by-day for the next week.
                    JSONObject dailyWeather = responseData.getJSONObject("daily");

                    //A data block containing the weather conditions for today.
                    JSONObject currentDayWeather = new JSONObject();

                    //simple date format used to match the date only (i.e. without time).
                    SimpleDateFormat simpleDateFormatObject = new SimpleDateFormat("yyyyMMdd");

                    //extract current date.
                    Date currentDate = new Date(Long.parseLong(String.valueOf(currentWeatherData.get("time")))*1000);

                    //String value of current date as yyyyMMdd
                    String currentDateString = simpleDateFormatObject.format(currentDate);

                    //Loop through list of daily weather forecast (i.e. day to day forcase for today and next week)
                    //and extract forecast data for today.
                    for(Object dailyWeatherObject : dailyWeather.getJSONArray("data")){
                        //check if the data for dailyWeatherObject is for today.
                        JSONObject dailyWeatherData = (JSONObject) dailyWeatherObject;
                        Date thisDay = new Date(Long.parseLong(String.valueOf(dailyWeatherData.get("time")))*1000);
                        String thisDateString = simpleDateFormatObject.format(thisDay);

                        if(currentDateString.equals(thisDateString)){
                            //forecast data for today is found, so exit.
                            currentDayWeather = dailyWeatherData;
                            break;
                        }
                    }

                    //extract required information.
                    String currentWeatherSummaryString = String.valueOf(currentWeatherData.get("summary"));
                    String todayWeatherSummaryString = String.valueOf(currentDayWeather.get("summary"));
                    double todayPrecipProbability = Double.parseDouble(String.valueOf(currentDayWeather.get("precipProbability"))) * 100;

                    toReturn.put("currentWeatherSummary", currentWeatherSummaryString);
                    toReturn.put("todayWeatherSummary", todayWeatherSummaryString);
                    toReturn.put("todayPrecipProbability", todayPrecipProbability+"");
                }

            }else{
                //if response status is not 200 then it was not successful.
                System.out.println("Request Failed with: \nResponse code: "+jsonResponse.getStatus()+"\nResponse message: "+jsonResponse.getStatusText());

            }

            //Determines if the request was successful of failed.
            toReturn.put("responseStatus",jsonResponse.getStatus()+"");

        } catch (UnirestException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return  toReturn;
    }

    /**
     * To update the api key.
     * @param apiKey
     */
    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    /**
     * To update the coordinates information URL
     * @param coordinatesUrl
     */
    public static void setCorrdinatesUrl(String coordinatesUrl) {
        COORDINATES_URL = coordinatesUrl;
    }

    /**
     * Retrieves the json file from the CORRDINATES_URL and maps city names to their repective coordinates.
     */
    private void requestCoordinatesData(){
        try {
            HttpResponse<JsonNode>  jsonResponse = Unirest.get(COORDINATES_URL).asJson();

            for(Object obj: jsonResponse.getBody().getArray()){
                JSONObject jsonObject = (JSONObject) obj;

                this.locationsToCoordinatesMap.put(jsonObject.get("name").toString().toLowerCase(), new Coordinate(jsonObject.get("lat").toString(),""+jsonObject.get("lng").toString()));
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    /**
     * To update the API URL.
     * @param latitude
     * @param longitude
     * @return API URL
     */
    private String getAPIURL(String latitude, String longitude){
        return "https://api.darksky.net/forecast/"+this.API_KEY+"/"+ latitude +","+ longitude+"?exclude=[minutely,alerts,flags]";
    }


    public static void main(String[] args) {
        WeatherDataProvider weatherDataProviderObject = new WeatherDataProvider();
        HashMap<String, String> res = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Please select from one of the following options:");
            System.out.println("Type 1 for the default weather information.");
            System.out.println("Type 2 for the weather information for a given city name.");
            System.out.println("Type 3 for the weather information for a given coordinates");
            System.out.println("Type 4 to quit!");

            String userInput = scanner.next();

            try{

                if(userInput.equals("1")){
                    res = weatherDataProviderObject.getWeatherData();
                }else if(userInput.equals("2")){
                    System.out.print("City name: ");
                    String cityName = scanner.next();
                    res = weatherDataProviderObject.getWeatherData(cityName);
                }else if(userInput.equals("3")){
                    System.out.print("Latitude: ");
                    String lat = scanner.next();
                    System.out.print("longitude: ");
                    String lng = scanner.next( );
                    res = weatherDataProviderObject.getWeatherData(lat, lng);
                }else if(userInput.equals("4")){
                    System.out.println("Bye Bye!");
                    break;
                }else{
                    System.out.println("invalid option! Try again");
                    continue;
                }


            }catch (Exception ex){
                System.out.println(ex.getMessage());
                continue;
            }

            if(res.get("responseStatus").equals("200")){
                String toPrint = "\nCurrent weather - "+res.get("currentWeatherSummary")+", Today we will see - "+
                        res.get("todayWeatherSummary").substring(0, res.get("todayWeatherSummary").length()-1)+" with a " + res.get("todayPrecipProbability") + "% chance of rain.\n";
                System.out.println(toPrint);
            }

        }

    }

}
