/**
 * Coordinate class stores coordinates for a specific location
 */

public class Coordinate {
    private String latitude, longitude;

    public Coordinate(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}