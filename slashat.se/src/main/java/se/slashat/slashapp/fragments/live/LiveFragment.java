package se.slashat.slashapp.fragments.live;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.LiveFullscreenActivity;
import se.slashat.slashapp.R;
import se.slashat.slashapp.live.BambuserController;
import se.slashat.slashapp.util.DisplaySize;

/**
 * Created by nicklas on 6/18/13.
 */
public class LiveFragment extends Fragment {
    private VideoView videoView;
    private LinearLayout.LayoutParams paramsNotFullscreen;
    private boolean livestreamStarted;

    //@TODO: Handle rotation.
    //@TODO: Continue play when switching fragments.
    //@TODO: Start after pressing a playbutton.
    //@TODO: Better Mediacontroller.


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_fragment, null);

        Button button = (Button) view.findViewById(R.id.livebutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LiveFullscreenActivity.class);

                startActivity(intent);
            }
        });

        return view;

    }
}
