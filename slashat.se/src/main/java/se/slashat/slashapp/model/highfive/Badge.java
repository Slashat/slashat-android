package se.slashat.slashapp.model.highfive;

import java.io.Serializable;

/**
 * Created by nicklas on 6/28/14.
 */
public class Badge implements Serializable{


    private final String name;
    private final String picture;
    private final String pictureLarge;
    private final String description;

    public Badge(String name, String picture, String pictureLarge, String description) {
        this.name = name;
        this.picture = picture;
        this.pictureLarge = pictureLarge;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureLarge() {
        return pictureLarge;
    }
}
