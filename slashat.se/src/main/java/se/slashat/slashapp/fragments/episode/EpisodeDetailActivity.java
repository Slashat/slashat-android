package se.slashat.slashapp.fragments.episode;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.util.ContentView;

/**
 * Created by nicklas on 8/14/13.
 */
public class EpisodeDetailActivity extends ActionBarActivity {

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

        getSupportFragmentManager().beginTransaction().add(ContentView.getContentViewCompat(), episodeDetailFragment).commit();

        setTitle(episode.getFullEpisodeName());

        ActionBar ab = getSupportActionBar();

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.pop_enter,R.anim.pop_exit);
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pop_enter,R.anim.pop_exit);
    }
}
