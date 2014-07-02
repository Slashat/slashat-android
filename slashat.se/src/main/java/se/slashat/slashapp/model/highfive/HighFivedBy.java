package se.slashat.slashapp.model.highfive;

import java.io.Serializable;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighFivedBy implements Serializable{
    private final String name;
    private final String date;
    private final String location;

    public HighFivedBy(String name, String date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
