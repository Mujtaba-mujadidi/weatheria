/**
 * A simple driver class to interact with the weather data provider for demonistation purposes.
 */


import java.util.HashMap;
import java.util.Scanner;

public class WeatheriaDriver {

    public static void main(String[] args) {
        WeatherDataProvider ws = new WeatherDataProvider();
        HashMap<String, String> res = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Please select from one of the following options:");
            System.out.println("Type 1 for the default weather information.");
            System.out.println("Type 2 for the weather information for a given city name.");
            System.out.println("Type 3 for the weather information for a given coordinates");
            System.out.println("Type 4 to quit!");

            String userInput = scanner.next();

            if(userInput.equals("1")){
                res = ws.getWeatherData();
            }else if(userInput.equals("2")){
                System.out.print("City name: ");
                String cityName = scanner.next();

                try {
                    res = ws.getWeatherData(cityName);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }else if(userInput.equals("3")){
                System.out.print("Latitude: ");
                String lat = scanner.next();
                System.out.print("longitude: ");
                String lng = scanner.next( );
                res = ws.getWeatherData(lat, lng);

            }else if(userInput.equals("4")){
                System.out.println("Bye Bye!");
                break;
            }

            String toPrint = "\nCurrent weather - "+res.get("currentWeatherSummary")+", Today we will see - "+
                    res.get("todayWeatherSummary").substring(0, res.get("todayWeatherSummary").length()-1)+" with a " + res.get("todayPrecipProbability") + "% chance of rain.\n";

            //System.out.println(toPrint);
        }
    }


}
