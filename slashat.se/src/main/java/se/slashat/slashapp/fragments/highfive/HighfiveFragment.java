package se.slashat.slashapp.fragments.highfive;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.highfive.HighFivedBy;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighfiveFragment extends Fragment {
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            //Call async when adding webservice
            User user = HighFiveService.getUser();

            view = inflater.inflate(R.layout.highfive_fragment, null);
            if (user != null){

                setText(view.findViewById(R.id.highfive_username),user.getName());
                setText(view.findViewById(R.id.highfive_numberofhighfives),user.getHighFivers().size()+" highfives");
                setText(view.findViewById(R.id.highfive_firsthighfive), formatFirstHighfivedBy(user.getHighFivedBy()));
                    setImage(view.findViewById(R.id.highfive_userimage), R.drawable.nicklas);
            }
        } catch (InflateException e) {
        }
        return view;
    }

    private String formatFirstHighfivedBy(HighFivedBy highFivedBy) {
        String base = "Fick sin första high-five av %s den %s i %s";
        return String.format(base,highFivedBy.getName(),highFivedBy.getDate(),highFivedBy.getLocation());
    }


    private void setText(View view, String text) {
        ((TextView) view).setText(text);
    }

    private void setImage(View view, int resource) {
        ((ImageView)view).setImageResource(resource);
    }
    // TODO: Load async image
    private void setImage(View view, URL imageUrl) {

    }

}
