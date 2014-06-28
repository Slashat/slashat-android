package se.slashat.slashapp.model.highfive;

import java.net.URL;

/**
 * Created by nicklas on 6/28/14.
 */
public class Badge {


    public final String name;
    public final String picture;

    public Badge(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }
}
