package ca.bcit.wester;

/**
 * Created by Chonjou on 2017-10-17.
 */

public class Location {
    private double longitude;
    private double latitude;
    private double altitude;

    public Location(double longi, double lat, double alt) {
        longitude = longi;
        latitude = lat;
        altitude = alt;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
