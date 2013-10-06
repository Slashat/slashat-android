package se.slashat.slashapp.viewmodel;

import java.net.URL;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.model.highfive.HighFiver;

public class AboutViewModel extends ViewModelBase<Object> {

    public String name;
    public String title;
    public int image;
    public URL imageUrl;

	public AboutViewModel(Personal p) {
        super(p);
        name = p.getName();
        title = p.getTitle();
        image = p.getImg();
        imageUrl = null;

	}

    public AboutViewModel(HighFiver p) {
        super(p);
        name = p.getName();
        title = p.getUsername();
        //Get real user image async
        image = 0;
        imageUrl = p.getPicture();

    }


    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public int getImg(){
        return image;
    }

    public URL getImageUrl() {
        return imageUrl;
    }
}
