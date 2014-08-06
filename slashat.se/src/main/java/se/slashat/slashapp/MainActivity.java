package se.slashat.slashapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.fragments.about.AboutFragment;
import se.slashat.slashapp.fragments.episode.EpisodeFragment;
import se.slashat.slashapp.fragments.highfive.HighfiveFragment;
import se.slashat.slashapp.fragments.live.LiveFragment;
import se.slashat.slashapp.player.PlayerInterfaceImpl;
import se.slashat.slashapp.service.HighFiveService;
import se.slashat.slashapp.util.Conversion;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static final String SELECTEDTAB = "selectedtab";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private LiveFragment mLiveFragment;
    private EpisodeFragment mEpisodeFragment;
    private HighfiveFragment mHighfiveFragment;
    private AboutFragment mAboutFragment;
    private static Context context;
    private PlayerInterfaceImpl playerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
        setContentView(R.layout.activity_main);

        FragmentSwitcher.initalize(getSupportFragmentManager(), this);
        HighFiveService.initalize(this);
        Conversion.init(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int i = 0; i < 4; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(getTabTitle(i))
                            .setTabListener(this));
        }
        // in case of screen orientation
        if ( savedInstanceState != null ) {
            actionBar.setSelectedNavigationItem( savedInstanceState.getInt( SELECTEDTAB, 0 ) );
        }


        this.context = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        playerInterface = new PlayerInterfaceImpl(this);
        EpisodePlayer.initalize(this,playerInterface);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt(SELECTEDTAB, getSupportActionBar().getSelectedNavigationIndex() );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        Fragment fragment = null;
        switch (tab.getPosition()){
            case 0:
                if (mLiveFragment == null){
                    mLiveFragment = new LiveFragment();
                }
                fragment = mLiveFragment;
                break;

            case 1:
                if (mEpisodeFragment == null){
                    mEpisodeFragment = new EpisodeFragment();
                }
                fragment = mEpisodeFragment;
                break;
            case 2:
                if (mAboutFragment == null){
                    mAboutFragment = new AboutFragment();
                }
                fragment = mAboutFragment;
                break;
            case 3:
                if (mHighfiveFragment == null){
                    mHighfiveFragment = new HighfiveFragment();
                }
                fragment = mHighfiveFragment;
                break;
        }
        FragmentSwitcher.getInstance().switchFragment(fragment,false,R.id.fragment_container);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }


    private String getTabTitle(int position){
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return "HIGH-FIVE!".toUpperCase(l);
        }

        return "";
    }
    public static Context getContext() {
        return context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {

            HighFiveService.setHighFive(scanResult.getContents(),new Callback<Boolean>() {
                @Override
                public void call(Boolean result) {
                    if (result){
                        Toast.makeText(MainActivity.this,"Yay, High-Five!",Toast.LENGTH_LONG).show();
                        mHighfiveFragment.reload();
                    }else{
                        Toast.makeText(MainActivity.this,"Aj då, det där gick inte så bra. Försök igen!",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
