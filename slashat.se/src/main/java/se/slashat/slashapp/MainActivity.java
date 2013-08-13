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
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.cast.CastContext;
import com.google.cast.CastDevice;
import com.google.cast.MediaRouteAdapter;
import com.google.cast.MediaRouteHelper;
import com.google.cast.MediaRouteStateChangeListener;



import android.support.v4.app.FragmentActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;

import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.fragments.about.AboutFragment;
import se.slashat.slashapp.fragments.episode.EpisodeFragment;
import se.slashat.slashapp.fragments.live.LiveFragment;
import se.slashat.slashapp.player.PlayerInterfaceImpl;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, MediaRouteAdapter {

    public static final String SELECTEDTAB = "selectedtab";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private LiveFragment mLiveFragment;
    private EpisodeFragment mEpisodeFragment;
    private AboutFragment mAboutFragment;
    private static Context context;
    private PlayerInterfaceImpl playerInterface;
    private MediaRouteButton mMediaRouteButton;
    private CastContext mCastContext;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MyMediaRouterCallback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private MediaRouteStateChangeListener mRouteStateListener;

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
    protected void onStop() {
        super.onStop();

        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaRouteHelper.unregisterMediaRouteProvider(mCastContext);
        mCastContext.dispose();
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

        MenuItem mediaRouteItem = menu.findItem( R.id.action_mediaroute );
        mediaRouteItem.getActionView().setBackgroundColor(R.color.abc_search_url_text_normal);

        mMediaRouteButton = (MediaRouteButton) mediaRouteItem.getActionView();

        mCastContext = new CastContext( getApplicationContext() );
        MediaRouteHelper.registerMinimalMediaRouteProvider( mCastContext, this );
        mMediaRouter = MediaRouter.getInstance( getApplicationContext() );
        mMediaRouteSelector = MediaRouteHelper.buildMediaRouteSelector( MediaRouteHelper.CATEGORY_CAST );
        mMediaRouteButton.setRouteSelector( mMediaRouteSelector );
        mMediaRouterCallback = new MyMediaRouterCallback();

        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);


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

    @Override
    public void onDeviceAvailable(CastDevice castDevice, String s, MediaRouteStateChangeListener mediaRouteStateChangeListener) {
        mSelectedDevice = castDevice;
        mRouteStateListener = mediaRouteStateChangeListener;
    }

    @Override
    public void onSetVolume(double v) {

    }

    @Override
    public void onUpdateVolume(double v) {

    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteSelected(MediaRouter router, RouteInfo route) {
            MediaRouteHelper.requestCastDeviceForRoute(route);
        }

        @Override
        public void onRouteUnselected(MediaRouter router, RouteInfo route) {
            mSelectedDevice = null;
            mRouteStateListener = null;
        }
    }

}
