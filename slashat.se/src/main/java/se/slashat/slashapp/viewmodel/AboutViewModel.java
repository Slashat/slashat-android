package se.slashat.slashapp.viewmodel;

import java.net.URL;

import se.slashat.slashapp.model.Crew;
import se.slashat.slashapp.model.highfive.HighFiver;

public class AboutViewModel extends DisplayRowViewModel<Object> {


    public enum Type {
        CREW,
        HIGHFIVER;
    }

    public String name;
    public String title;
    public int image;
    public URL imageUrl;
    public Type type;
    private Crew crew;

    public AboutViewModel(Crew p) {
        super(p);
        name = p.getName();
        title = p.getTitle();
        image = p.getImg();
        imageUrl = null;
        type = Type.CREW;
        crew = p;
    }

    public AboutViewModel(HighFiver p) {
        super(p);
        name = p.getUsername();
        title = "";
        imageUrl = p.getPicture();
        type = Type.HIGHFIVER;
    }


    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getImg() {
        return image;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public Type getType() {
        return type;
    }

    public Crew getCrew() {
        return crew;
    }
}
