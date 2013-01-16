package se.slashat.slashat;

import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.androidservice.EpisodePlayer.EpisodePlayerBinder;
import se.slashat.slashat.fragment.AboutFragment;
import se.slashat.slashat.fragment.ArchiveFragment;
import se.slashat.slashat.fragment.FragmentSwitcher;
import se.slashat.slashat.fragment.LiveFragment;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar.Tab;

import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener, FragmentSwitcher {

	private static final String TAG = "Slashat";
	private static LiveFragment liveFrag;
	private EpisodePlayer episodePlayer;
	private boolean episodePlayerBound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initiate our fragments
		if (savedInstanceState == null) {
			// initial fragment objects
			liveFrag = new LiveFragment();
		}

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
		bindToEpisodePlayerService();
	}

	public void switchFragment(Fragment fragment, boolean addToBackstack) {
		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction.replace(android.R.id.content, fragment);
		if (addToBackstack) {
			beginTransaction.addToBackStack(null);
		} else {
			getSupportFragmentManager().popBackStack();
		}
		beginTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		beginTransaction.commit();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO: Rewrite this section to a fragment-container and call fragments
		// based on their classnames and tab ids

		if (tab.getPosition() == 0) {
			// live
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-live");
			switchFragment(liveFrag, false);
		} else if (tab.getPosition() == 1) {
			// archive
			Log.d(TAG, "Loading fragment for: " + tab.getPosition()
					+ "-archive");
			Bundle bundle = new Bundle();
			bundle.putSerializable(ArchiveFragment.EPISODEPLAYER, episodePlayer);
			ArchiveFragment archiveFragment = new ArchiveFragment();
			archiveFragment.setArguments(bundle);
			switchFragment(archiveFragment, false);
		} else if (tab.getPosition() == 2) {
			// about
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			Bundle bundle = new Bundle();
			bundle.putSerializable(AboutFragment.FRAGMENTSWITCHER, this);
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

	/**
	 * Starts up the Episode player service and binds it.
	 */
	private void bindToEpisodePlayerService() {
		Intent intent = new Intent(this, EpisodePlayer.class);
		if (EpisodePlayerRunning()) {
			getApplicationContext().bindService(intent,
					episodePlayerConnection, Context.BIND_AUTO_CREATE);
		} else {
			getApplicationContext().startService(intent);
			getApplicationContext().bindService(intent,
					episodePlayerConnection, Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * Stop the Episode player service.
	 */

	private void unBindEpisodePlayerService() {
		if (episodePlayerBound) {
			episodePlayer.stopPlay();
			getApplicationContext().unbindService(episodePlayerConnection);
			episodePlayerBound = false;
		}

		Intent intent = new Intent(this, EpisodePlayer.class);
		getApplicationContext().stopService(intent);
	}

	/**
	 * Checks if the Episode Player Service is running or not
	 * 
	 * @return
	 */
	private boolean EpisodePlayerRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("se.slashat.slashat.slashat.androidservice.EpisodePlayer"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The connection to the Episode player service used to control the service
	 * itself (not the player).
	 */
	private ServiceConnection episodePlayerConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			EpisodePlayerBinder binder = (EpisodePlayerBinder) service;
			episodePlayer = binder.getService();
			episodePlayerBound = true;

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			episodePlayerBound = false;
		}
	};
}
