package se.slashat.slashapp.model.highfive;

import java.net.URL;

/**
 * Created by nicklas on 6/28/14.
 */
public class Achivement
{
    public final String name;
    public final URL picture;
    public final boolean achieved;

    public Achivement(String name, URL picture, boolean achieved) {
        this.name = name;
        this.picture = picture;
        this.achieved = achieved;
    }

    public String getName() {
        return name;
    }

    public URL getPicture() {
        return picture;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
