package se.slashat.slashapp.model.highfive;

import java.util.Date;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighFivedBy{
    private final String name;
    private final Date date;
    private final String location;

    public HighFivedBy(String name, Date date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
