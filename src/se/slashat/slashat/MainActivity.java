package se.slashat.slashat;

import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.fragment.AboutFragment;
import se.slashat.slashat.fragment.ArchiveFragment;
import se.slashat.slashat.fragment.FragmentSwitcher;
import se.slashat.slashat.fragment.LiveFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener{

	private static final String TAG = "Slashat";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initiate our fragments
		
		FragmentSwitcher.initalize(getSupportFragmentManager());
		EpisodePlayer.initalize(getApplicationContext());

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab liveTab = getSupportActionBar().newTab();
		liveTab.setText("Live");
		ActionBar.Tab archiveTab = getSupportActionBar().newTab();
		archiveTab.setText("Showarkiv");
		ActionBar.Tab aboutTab = getSupportActionBar().newTab();
		aboutTab.setText("Om Slashat");

		liveTab.setTabListener(this);
		archiveTab.setTabListener(this);
		aboutTab.setTabListener(this);

		getSupportActionBar().addTab(liveTab);
		getSupportActionBar().addTab(archiveTab);
		getSupportActionBar().addTab(aboutTab);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void switchFragment(Fragment fragment, boolean addToBackstack) {
		FragmentSwitcher.getInstance().switchFragment(fragment, addToBackstack);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO: Rewrite this section to a fragment-container and call fragments
		// based on their classnames and tab ids

		if (tab.getPosition() == 0) {
			// live
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-live");
			switchFragment(new LiveFragment(), false);
		} else if (tab.getPosition() == 1) {
			// archive
			Log.d(TAG, "Loading fragment for: " + tab.getPosition()
					+ "-archive");
			Bundle bundle = new Bundle();
			//bundle.putSerializable(ArchiveFragment.EPISODEPLAYER, episodePlayer);
			ArchiveFragment archiveFragment = new ArchiveFragment();
			archiveFragment.setArguments(bundle);
			switchFragment(archiveFragment, false);
		} else if (tab.getPosition() == 2) {
			// about
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			Bundle bundle = new Bundle();
			//bundle.putSerializable(AboutFragment.FRAGMENTSWITCHER, this);
			AboutFragment aboutFragment = new AboutFragment();
			aboutFragment.setArguments(bundle);
			switchFragment(aboutFragment, false);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}
}
