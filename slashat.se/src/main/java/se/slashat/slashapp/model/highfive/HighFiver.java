package se.slashat.slashapp.model.highfive;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighFiver implements Serializable{

    private final String userId;
    private final String username;
    private final URL picture;
    private final long highfivedDate;


    public HighFiver(String userId, String username, URL picture, long highfivedDate) {
        this.userId = userId;
        this.username = username;
        this.picture = picture;
        this.highfivedDate = highfivedDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public URL getPicture() {
        return picture;
    }

    public long getHighfivedDate() {
        return highfivedDate;
    }
}
