package se.slashat.slashapp;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.fragments.about.AboutFragment;
import se.slashat.slashapp.fragments.episode.EpisodeFragment;
import se.slashat.slashapp.fragments.live.LiveFragment;
import se.slashat.slashapp.player.PlayerInterfaceImpl;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final String SELECTEDTAB = "selectedtab";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private LiveFragment mLiveFragment;
    private EpisodeFragment mEpisodeFragment;
    private AboutFragment mAboutFragment;
    private static Context context;
    private PlayerInterfaceImpl playerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        FragmentSwitcher.initalize(getSupportFragmentManager(), this);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int i = 0; i < 3; i++) {
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
    protected void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        outState.putInt(SELECTEDTAB, getActionBar().getSelectedNavigationIndex() );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
        }
        //getSupportFragmentManager().popBackStack();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        FragmentSwitcher.getInstance().switchFragment(fragment,false,R.id.fragment_container);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
        }

        return "";
    }
    public static Context getContext() {
        return context;
    }
}
