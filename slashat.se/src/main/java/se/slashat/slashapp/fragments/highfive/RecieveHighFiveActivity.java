package se.slashat.slashapp.fragments.highfive;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import se.slashat.slashapp.R;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.util.ContentView;

/**
 * Created by nicklas on 10/23/13.
 */
public class RecieveHighFiveActivity extends ActionBarActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().containsKey("user")) {
            user = (User) getIntent().getExtras().getSerializable("user");
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        RecieveHighFiveFragment recieveHighFiveFragment = new RecieveHighFiveFragment();
        recieveHighFiveFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(ContentView.getContentViewCompat(), recieveHighFiveFragment).commit();

        setTitle("Ta emot High-Five!");

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
