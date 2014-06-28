package se.slashat.slashapp.model.highfive;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by nicklas on 6/28/14.
 */
public class Achivement extends Badge implements Serializable
{
    public final boolean achieved;

    public Achivement(String name, String picture, boolean achieved) {
        super(name, picture);
        this.achieved = achieved;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
