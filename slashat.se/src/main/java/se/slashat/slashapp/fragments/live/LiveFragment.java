package se.slashat.slashapp.fragments.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.util.List;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.LiveFullscreenActivity;
import se.slashat.slashapp.R;
import se.slashat.slashapp.model.LiveEvent;
import se.slashat.slashapp.service.GoogleCalendarService;

/**
 * Created by nicklas on 6/18/13.
 */
public class LiveFragment extends Fragment {
    private VideoView videoView;
    private LinearLayout.LayoutParams paramsNotFullscreen;
    private boolean livestreamStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.live_fragment, null);

        return view;
    }
}
