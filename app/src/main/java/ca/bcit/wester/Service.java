package ca.bcit.wester;

/**
 * Created by Chonjou on 2017-10-17.
 */

public class Service {
    private Location location;
    private String name;
    private String category;

    public Service(double longi, double lat, double alt, String name, String category) {
        this.location = new Location(longi, lat, alt);
        this.name = name;
        this.category = category;
    }
}
