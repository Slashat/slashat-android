package se.slashat.slashapp.viewmodel;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Personal;
import se.slashat.slashapp.model.highfive.HighFiver;

public class AboutViewModel extends ViewModelBase<Object> {

    public String name;
    public String title;
    public int image;

	public AboutViewModel(Personal p) {
        super(p);
        name = p.getName();
        title = p.getTitle();
        image = p.getImg();

	}

    public AboutViewModel(HighFiver p) {
        super(p);
        name = p.getName();
        title = p.getUserId();
        //Get real user image async
        image = R.drawable.nicklas;
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
}
