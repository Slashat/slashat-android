package se.slashat.slashat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.androidservice.EpisodePlayer.PlayerInterface;
import se.slashat.slashat.async.EpisodeLoaderAsyncTask;
import se.slashat.slashat.async.EpisodeLoaderAsyncTask.UpdateCallback;
import se.slashat.slashat.fragment.AboutFragment;
import se.slashat.slashat.fragment.ArchiveFragment;
import se.slashat.slashat.fragment.FragmentSwitcher;
import se.slashat.slashat.fragment.LiveFragment;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.service.ArchiveService;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.opengl.Visibility;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

	public static final String TAG = "Slashat";
	private static Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainActivity.context = getApplicationContext();
		// initiate our fragments
		setContentView(R.layout.activity_main);
		FragmentSwitcher.initalize(getSupportFragmentManager());
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

		EpisodePlayer.initalize(getApplicationContext(), new PlayerInterfaceImpl(this));
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
			LinearLayout playerLayout = (LinearLayout) findViewById(R.layout.playerLayout);
			playerLayout.setVisibility(View.GONE);
		} else if (tab.getPosition() == 1) {
			// archive
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-archive");
			Bundle bundle = new Bundle();
			// bundle.putSerializable(ArchiveFragment.EPISODEPLAYER,
			// episodePlayer);
			ArchiveFragment archiveFragment = new ArchiveFragment();
			archiveFragment.setArguments(bundle);
			switchFragment(archiveFragment, false);
			LinearLayout playerLayout = (LinearLayout) findViewById(R.layout.playerLayout);
			playerLayout.setVisibility(View.VISIBLE);
		} else if (tab.getPosition() == 2) {
			// about
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			Bundle bundle = new Bundle();
			// bundle.putSerializable(AboutFragment.FRAGMENTSWITCHER, this);
			AboutFragment aboutFragment = new AboutFragment();
			aboutFragment.setArguments(bundle);
			switchFragment(aboutFragment, false);
			LinearLayout playerLayout = (LinearLayout) findViewById(R.layout.playerLayout);
			playerLayout.setVisibility(View.VISIBLE);			
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
	 * position from the player and updates the {@link SeekBar}. When the user
	 * moves the {@link SeekBar} position and releases it the player skips to
	 * that position.
	 * 
	 * Currently we are using the Android resource for buttons. Those might look
	 * wrong on older versions of Android. In that case replace with our own
	 * resources instead.
	 * 
	 */
	private final class PlayerInterfaceImpl implements PlayerInterface, OnSeekBarChangeListener, OnClickListener {
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
		ImageButton button = (ImageButton) findViewById(R.id.playPauseButton);
		private int newPosition;
		private Dialog seekbarOverlay;
		private boolean seekbarOverlayShowing = false;
		private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		private TextView textView;
		private Activity callingActivity;

		public PlayerInterfaceImpl(Activity callingActivity) {
			this.callingActivity = callingActivity;
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			seekBar.setOnSeekBarChangeListener(this);
			button.setOnClickListener(this);
		}

		@Override
		public void durationUpdate(final int seekMax, final int seek) {
			if (seekbarOverlayShowing){
				return;
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					seekBar.setMax(seekMax);
					seekBar.setProgress(seek);
					onMediaPlaying("");
					TextView durationTextView = (TextView) findViewById(R.id.durationTextView);
					if (durationTextView != null) {
						durationTextView.setText(dateFormat.format(new Date(seek)));
					}
				}
			});
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (seekbarOverlay != null && seekbarOverlay.isShowing() && textView != null) {
				updateOverlaySeekProgress(progress);
				updateOverlayPosition(seekBar, progress);
			}
			if (!fromUser) {
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
			overlayParams.y = location[1] - 100;
			seekbarOverlay.setCancelable(false);
			seekbarOverlay.setContentView(R.layout.player_overlay);
			seekbarOverlay.show();
			textView = (TextView) seekbarOverlay.findViewById(R.id.playeroverlay);
			seekbarOverlayShowing = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			EpisodePlayer.getEpisodePlayer().seek(newPosition);
			seekbarOverlayShowing = false;
			seekbarOverlay.hide();
		}

		@Override
		public void onMediaPaused(String episodeName) {
			button.setImageResource(android.R.drawable.ic_media_play);
			// maybe use setDrawable instead
		}

		@Override
		public void onMediaStopped(String episodeName, boolean EOF) {
			button.setImageResource(android.R.drawable.ic_media_play);
			// maybe use setDrawable instead
			Log.i(MainActivity.TAG,"Current: "+episodeName);
			if (EOF && episodeName != null && !episodeName.equals("")) {
				List<Episode> episodes = Arrays.asList(ArchiveService.getEpisodes(EpisodeLoaderAsyncTask.getVoidCallback()));
				
				Episode newEpisode = null;
				EPISODELOOP: for (Iterator<Episode> iterator = episodes.iterator(); iterator.hasNext();) {
					Episode e = iterator.next();
					Log.i(MainActivity.TAG, e.getName());
					String fullName = e.getFullEpisodeName();
					if (fullName.equals(episodeName)){
						newEpisode = iterator.next();
						break EPISODELOOP;
					}
					
				}
				
				if (newEpisode != null){
					ProgressDialog progressDialog = new ProgressDialog(callingActivity);
					progressDialog.setTitle("Buffrar avsnitt");
					progressDialog.setMessage(newEpisode.getFullEpisodeName());
					progressDialog.show();
					EpisodePlayer.getEpisodePlayer().initializePlayer(newEpisode.getStreamUrl(),
							newEpisode.getFullEpisodeName(), 0, progressDialog);
				}	
			}

		}

		@Override
		public void onMediaPlaying(String episodeName) {
			button.setImageResource(android.R.drawable.ic_media_pause);
			// maybe use setDrawable instead
		}

		@Override
		public void onClick(View v) {
			if (!EpisodePlayer.getEpisodePlayer().isMediaInitalized()) {

				String lastPlayedEpisodeName = EpisodePlayer.getEpisodePlayer().getLastPlayedEpisodeName();
				int lastPlayedPosition = EpisodePlayer.getEpisodePlayer().getLastPlayedPosition();
				String lastPlayedStreamUrl = EpisodePlayer.getEpisodePlayer().getLastPlayedStreamUrl();
				if (lastPlayedStreamUrl != null && !lastPlayedStreamUrl.equals("")) {
					// DRY ftw.. plz fix me...
					ProgressDialog progressDialog = new ProgressDialog(callingActivity);
					progressDialog.setTitle("Buffrar avsnitt");
					progressDialog.setMessage(lastPlayedEpisodeName);
					progressDialog.show();
					EpisodePlayer.getEpisodePlayer().initializePlayer(lastPlayedStreamUrl, lastPlayedEpisodeName, lastPlayedPosition, progressDialog);
					onMediaPlaying("");
				}
				return;
			}
			if (EpisodePlayer.getEpisodePlayer().isPlaying()) {
				EpisodePlayer.getEpisodePlayer().pause();
				onMediaPaused("");
			} else {
				EpisodePlayer.getEpisodePlayer().resume();
				onMediaPlaying("");
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
