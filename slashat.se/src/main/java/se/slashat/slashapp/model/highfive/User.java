package se.slashat.slashapp.model.highfive;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;

/**
 * Created by nicklas on 9/28/13.
 */
public class User implements Serializable{
    private final String userName;
    private final String userId;
    private final HighFivedBy highFivedBy;
    private final URL picture;
    private final URL qr;
    private byte[] qrBitmap;
    private final Collection<HighFiver> highFivers;
    private final Collection<Achivement> achivements;
    private final Collection<Badge> badges;


    public User(String userName, String userId, HighFivedBy highFivedBy, URL picture, URL qr, String qrcode_id, Collection<HighFiver> highFivers, Collection<Achivement> achivements, Collection<Badge> badges) {
        this.userName = userName;
        this.userId = userId;
        this.highFivedBy = highFivedBy;
        this.picture = picture;
        this.qr = qr;
        this.highFivers = highFivers;
        this.achivements = achivements;
        this.badges = badges;
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

    public Collection<HighFiver> getHighFivers() {
        return highFivers;
    }

    public void setQrBitmap(byte[] qrBitmap) {
        this.qrBitmap = qrBitmap;
    }

    public byte[] getQrBitmap() {
        return qrBitmap;
    }

    public Collection<Achivement> getAchivements() {
        return achivements;
    }

    public Collection<Badge> getBadges() {
        return badges;
    }
}
