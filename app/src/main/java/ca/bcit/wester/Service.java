package ca.bcit.wester;

/**
 * a
 * Created by Chonjou on 2017-10-17.
 */

public class Service {
    private Location location;
    private String name;
    private String category;

    public Service(double longit, double lat, double alt, String name, String category) {
        this.location = new Location(longit, lat, alt);
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
