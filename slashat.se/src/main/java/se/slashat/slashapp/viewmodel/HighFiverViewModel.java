package se.slashat.slashapp.viewmodel;

import se.slashat.slashapp.model.highfive.HighFiver;

/**
 * Created by nicklas on 10/1/13.
 */
public class HighFiverViewModel extends DisplayRowViewModel<HighFiver>{
    private String userName;
    private String picture;

    public HighFiverViewModel(HighFiver model) {
        super(model);
        userName = model.getUsername();
        picture = model.getPicture().toString();
    }

    public String getUserName() {
        return userName;
    }

    public String getPicture() {
        return picture;
    }
}
