package se.slashat.slashapp.model.highfive;

import java.net.URL;
import java.util.Collection;

/**
 * Created by nicklas on 9/28/13.
 */
public class User {

    private final String name;
    private final String userName;
    private final String userId;
    private final HighFivedBy highFivedBy;
    private final URL picture;
    private final URL qr;
    private final Collection<HighFiver> highFivers;


    public User(String name, String userName, String userId, HighFivedBy highFivedBy, URL picture, URL qr, Collection<HighFiver> highFivers) {
        this.name = name;
        this.userName = userName;
        this.userId = userId;
        this.highFivedBy = highFivedBy;
        this.picture = picture;
        this.qr = qr;
        this.highFivers = highFivers;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public HighFivedBy getHighFivedBy() {
        return highFivedBy;
    }

    public URL getPicture() {
        return picture;
    }

    public URL getQr() {
        return qr;
    }
}
