package se.slashat.slashapp.fragments.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.slashat.slashapp.R;

/**
 * Created by nicklas on 6/18/13.
 */
public class AboutFragment extends Fragment {
    private static View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.about_fragment, null);
        }catch (InflateException e) {

        }

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = null;*/
    }
}
