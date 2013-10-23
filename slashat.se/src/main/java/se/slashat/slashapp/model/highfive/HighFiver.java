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


    public HighFiver(String userId, String username, URL picture) {
        this.userId = userId;
        this.username = username;
        this.picture = picture;
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
}
