package se.slashat.slashapp.fragments.episode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Episode;

/**
 * Created by nicklas on 6/18/13.
 */
public class EpisodeDetailFragment extends Fragment {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("sv"));
    private Episode episode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.episode_detail, null);
        if (episode != null) {
            setText(view.findViewById(R.id.episodeNumberAndTitle), "#" + episode.getEpisodeNumber() + " - " + episode.getName());
            setText(view.findViewById(R.id.dateAndLength), dateFormat.format(episode.getPublished()) + " - " + episode.getDuration());
            setText(view.findViewById(R.id.description), episode.getDescription());
            ((Button) view.findViewById(R.id.listenbutton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //playEpisode
                }
            });
            ((Button) view.findViewById(R.id.viewbutton)).setEnabled(false); // Disable view video button for now.

        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("episode")) {
            episode = (Episode) getArguments().getSerializable("episode");
        }
    }


    private void setText(View view, String text) {
        ((TextView) view).setText(text);
    }


}
