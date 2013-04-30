package se.slashat.slashat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.androidservice.EpisodePlayer.PlayerInterface;
import se.slashat.slashat.fragment.AboutFragment;
import se.slashat.slashat.fragment.ArchiveFragment;
import se.slashat.slashat.fragment.FragmentSwitcher;
import se.slashat.slashat.fragment.LiveFragment;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener {

	public static final String TAG = "Slashat";
	private static Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MainActivity.context = getApplicationContext();
		// initiate our fragments
		setContentView(R.layout.activity_main);
		FragmentSwitcher.initalize(getSupportFragmentManager());
		EpisodePlayer.initalize(getApplicationContext(),
				new PlayerInterfaceImpl());

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
	
	public static Context getContext() {
		return context;
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
			// bundle.putSerializable(ArchiveFragment.EPISODEPLAYER,
			// episodePlayer);
			ArchiveFragment archiveFragment = new ArchiveFragment();
			archiveFragment.setArguments(bundle);
			switchFragment(archiveFragment, false);
		} else if (tab.getPosition() == 2) {
			// about
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			Bundle bundle = new Bundle();
			// bundle.putSerializable(AboutFragment.FRAGMENTSWITCHER, this);
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
	 * Connection between UI elements and the Episodeplayer.
	 * 
	 * When play button is pressed we pause or resume. On play and pause events
	 * the button is updated. Every second we receive the duration and current
	 * position from the player and updates the {@link SeekBar}. When the user moves the
	 * {@link SeekBar} position and releases it the player skips to that position.
	 * 
	 * Currently we are using the Android resource for buttons. Those might look
	 * wrong on older versions of Android. In that case replace with our own
	 * resources instead.
	 * 
	 */
	private final class PlayerInterfaceImpl implements PlayerInterface,
			OnSeekBarChangeListener, OnClickListener {
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
		ImageButton button = (ImageButton) findViewById(R.id.playPauseButton);
		private int newPosition;
		private Dialog seekbarOverlay;
		private SimpleDateFormat dateFormat = new SimpleDateFormat(
				"HH:mm:ss", Locale.ENGLISH);
		private TextView textView;

		public PlayerInterfaceImpl() {
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			seekBar.setOnSeekBarChangeListener(this);
			button.setOnClickListener(this);
		}

		@Override
		public void durationUpdate(final int seekMax, final int seek) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					seekBar.setMax(seekMax);
					seekBar.setProgress(seek);
				}
			});
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (seekbarOverlay != null && seekbarOverlay.isShowing() && textView != null){
				updateOverlaySeekProgress(progress);
				updateOverlayPosition(seekBar, progress);
			}
			if (!fromUser){
				return;
			}				
			newPosition = progress;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			createAndShowSeekOverlay(seekBar);			
		}

		private void createAndShowSeekOverlay(SeekBar seekBar) {
			seekbarOverlay = new Dialog(MainActivity.this);
			seekbarOverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
			seekbarOverlay.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			seekbarOverlay.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			LayoutParams overlayParams = seekbarOverlay.getWindow().getAttributes();
			overlayParams.gravity = Gravity.TOP | Gravity.LEFT;
			overlayParams.x = (int) (seekBar.getLeft() + ((float) seekBar.getProgress() / (float) seekBar.getMax()) * seekBar.getWidth());
			int[] location = new int[2];
			seekBar.getLocationOnScreen(location);
			overlayParams.y = location[1]-100;
			seekbarOverlay.setCancelable(false);
			seekbarOverlay.setContentView(R.layout.player_overlay);
			seekbarOverlay.show();
			textView = (TextView) seekbarOverlay.findViewById(R.id.playeroverlay);
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			EpisodePlayer.getEpisodePlayer().seek(newPosition);
			seekbarOverlay.hide();
		}

		@Override
		public void onMediaPaused() {
			button.setImageResource(android.R.drawable.ic_media_play);
			// maybe use setDrawable instead
		}

		@Override
		public void onMediaStopped() {
			button.setImageResource(android.R.drawable.ic_media_play);
			// maybe use setDrawable instead
		}

		@Override
		public void onMediaPlaying() {
			button.setImageResource(android.R.drawable.ic_media_pause);
			// maybe use setDrawable instead
		}

		@Override
		public void onClick(View v) {
			if (EpisodePlayer.getEpisodePlayer().isPlaying()) {
				EpisodePlayer.getEpisodePlayer().pause();
				onMediaPaused();
			} else {
				EpisodePlayer.getEpisodePlayer().resume();
				onMediaPlaying();
			}
		}
		
		private void updateOverlayPosition(SeekBar seekBar, int progress) {
			LayoutParams overlayParams = seekbarOverlay.getWindow().getAttributes();
			Integer newX = (int) (seekBar.getLeft() + ((float) progress / (float) seekBar.getMax()) * seekBar.getWidth());
			overlayParams.x = newX;
			seekbarOverlay.getWindow().setAttributes(overlayParams);
		}

		private void updateOverlaySeekProgress(int progress) {
			Date date = new Date(progress);
			String newPosition = dateFormat.format(date);
			textView.setText(newPosition);
		}
	}
}
