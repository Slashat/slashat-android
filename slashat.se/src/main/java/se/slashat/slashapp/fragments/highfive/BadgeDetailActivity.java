package se.slashat.slashapp.fragments.highfive;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import se.slashat.slashapp.R;
import se.slashat.slashapp.util.ContentView;

/**
 * Created by nicklas on 6/29/14.
 */
public class BadgeDetailActivity  extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pictureLarge = "";
        String description = "";

        if (getIntent().getExtras().containsKey("pictureLarge") && getIntent().getExtras().containsKey("description")) {
            pictureLarge = getIntent().getExtras().getString("pictureLarge");
            description = getIntent().getExtras().getString("description");
        }

        Bundle bundle = new Bundle();
        bundle.putString("pictureLarge", pictureLarge);
        bundle.putString("description", description);


        BadgeDetailFragment badgeDetailFragment = new BadgeDetailFragment();
        badgeDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(ContentView.getContentViewCompat(), badgeDetailFragment).commit();

        setTitle("Information om troféen");

        ActionBar ab = getSupportActionBar();

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
    }
}