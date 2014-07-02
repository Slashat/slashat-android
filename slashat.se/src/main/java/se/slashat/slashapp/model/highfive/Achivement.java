package se.slashat.slashapp.model.highfive;

import java.io.Serializable;

/**
 * Created by nicklas on 6/28/14.
 */
public class Achivement extends Badge implements Serializable
{
    private final boolean achieved;
    private final String descriptionAchieved;

    public Achivement(String name, String picture, String pictureUrl, String description, String descriptionAchieved, boolean achieved) {
        super(name, picture, pictureUrl, description);
        this.achieved = achieved;
        this.descriptionAchieved = descriptionAchieved;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public String getDescriptionAchieved() {
        return descriptionAchieved;
    }
}
