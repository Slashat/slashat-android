package se.slashat.slashapp.fragments.highfive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.slashat.slashapp.R;

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
            view = inflater.inflate(R.layout.highfive_fragment, null);
        } catch (InflateException e) {

        }

        return view;
    }
}
