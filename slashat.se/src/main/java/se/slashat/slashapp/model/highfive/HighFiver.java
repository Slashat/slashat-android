package se.slashat.slashapp.model.highfive;

import java.net.URL;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighFiver {

    private final String name;
    private final String userId;
    private final String username;
    private final URL picture;


    public HighFiver(String name, String userId, String username, URL picture) {
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.picture = picture;
    }

    public String getName() {
        return name;
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
