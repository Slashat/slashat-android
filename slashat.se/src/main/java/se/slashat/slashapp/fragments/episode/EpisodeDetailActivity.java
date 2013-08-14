package se.slashat.slashapp.fragments.episode;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import se.slashat.slashapp.model.Episode;

/**
 * Created by nicklas on 8/14/13.
 */
public class EpisodeDetailActivity extends FragmentActivity {

    private Episode episode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().containsKey("episode")) {
            episode = (Episode) getIntent().getExtras().getSerializable("episode");
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("episode", episode);

        EpisodeDetailFragment episodeDetailFragment = new EpisodeDetailFragment();
        episodeDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(android.R.id.content, episodeDetailFragment).commit();

        setTitle(episode.getFullEpisodeName());

        ActionBar ab = getActionBar();

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }
}
