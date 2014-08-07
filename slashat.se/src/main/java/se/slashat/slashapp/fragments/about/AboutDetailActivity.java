package se.slashat.slashapp.fragments.about;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.Crew;
import se.slashat.slashapp.util.ContentView;

/**
 * Created by nicklas on 8/14/13.
 */
public class AboutDetailActivity extends ActionBarActivity {

    private Crew crew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().containsKey("person")) {
            crew = (Crew) getIntent().getExtras().getSerializable("person");
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("person", crew);

        AboutDetailFragment aboutDetailFragment = new AboutDetailFragment();
        aboutDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(ContentView.getContentViewCompat(), aboutDetailFragment).commit();

        setTitle(crew.getName());

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
