package se.slashat.slashat.androidservice;

import java.io.Serializable;

import se.slashat.slashat.MainActivity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

/**
 * EpisodePlayerService that plays a selected episode in the background. TODO:
 * Wake locks, MediaPlayerState handling, seek in file, interaction with
 * buttons.
 * 
 * @author nicklas.lof
 * 
 */

public class EpisodePlayer extends Service implements OnPreparedListener, OnCompletionListener,
		Serializable {

	private static final long serialVersionUID = 1L;
	private MediaPlayer mediaPlayer;
	private EpisodePlayerBinder binder;
	private boolean episodePlayerBound;
	private static EpisodePlayer episodePlayer;
	private Notification notification;
	private String episodeName;
	private ProgressDialog progressDialog;
	private static PlayerInterface playerInterface;
	private DurationUpdaterThread durationUpdaterThread;
	private static boolean paused = false;

	public class EpisodePlayerBinder extends Binder implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EpisodePlayer getService() {
			return EpisodePlayer.this;
		}
	}

	/**
	 * Callback interface to inform caller about duration updates and update
	 * media player status.
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	public interface PlayerInterface {
		public void durationUpdate(int seekMax, int seek);

		public void onMediaPaused();

		public void onMediaStopped();

		public void onMediaPlaying();
	}

	public EpisodePlayer() {

	}

	/**
	 * Initalizes the episoderplayer and binds it as a service. It seems to need
	 * the applicationContext from the MainActivity to work correctly.
	 * 
	 * @param context
	 */
	public static void initalize(Context context,
			PlayerInterface playerInterface) {
		EpisodePlayer.playerInterface = playerInterface;
		new EpisodePlayer().bindToEpisodePlayerService(context);
	}

	/**
	 * Gets the episodeplayer.
	 * 
	 * @return
	 */
	public static EpisodePlayer getEpisodePlayer() {
		if (episodePlayer == null) {
			throw new IllegalStateException(
					"Please initalize the EpisodePlayer service before trying to use it");
		}
		return episodePlayer;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		binder = new EpisodePlayerBinder();
	}

	/**
	 * Starts a new Mediaplayer for the selected URL.
	 * 
	 * @param streamUrl
	 * @param episodeName
	 */
	public void initializePlayer(String streamUrl, String episodeName,
			ProgressDialog progressDialog) {
		this.episodeName = episodeName;
		this.progressDialog = progressDialog;

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		if (streamUrl == null) {
			stopPlay(); // Temporary to stop playing until buttons are
						// implemented.
			progressDialog.dismiss();
			return;
		}
		try {
			mediaPlayer.setDataSource(streamUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.prepareAsync();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		startPlay();
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		durationUpdaterThread.interupt();
		playerInterface.onMediaStopped();
	}

	/**
	 * Start play the media set in initializePlayer() and plays the media in the
	 * background.
	 */
	private void startPlay() {

		setNotification();
		mediaPlayer.start();
		progressDialog.dismiss();

		durationUpdaterThread = new DurationUpdaterThread(mediaPlayer,
				playerInterface);
		durationUpdaterThread.start();

		playerInterface.onMediaPlaying();

	}

	/**
	 * Stops playing the current media and removes the player from the
	 * background
	 */
	public void stopPlay() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			unsetNotification();
		}
		paused = false;
		playerInterface.onMediaStopped();
	}

	public void seek(int position) {
		mediaPlayer.seekTo(position);
	}

	public void pause() {
		paused = true;
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			unsetNotification();
		}
	}

	public void resume() {
		paused = false;
		setNotification();
		mediaPlayer.start();
	}
	
	private void setNotification(){
		// TODO Replace with Non deprecated way to create notifications
		notification = new Notification(android.R.drawable.ic_media_play,
				"slashat.se", System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		CharSequence contentTitle = "slashat.se spelar nu";
		CharSequence contentText = episodeName;

		notification.setLatestEventInfo(getApplicationContext(), contentTitle,
				contentText, pendingIntent);

		startForeground(1, notification);
	}
	
	private void unsetNotification(){
		stopForeground(true);
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public boolean isPaused() {
		return paused;
	}

	private void bindToEpisodePlayerService(Context context) {
		Intent intent = new Intent(context, EpisodePlayer.class);
		if (isEpisodePlayerRunning(context)) {
			context.bindService(intent, episodePlayerConnection,
					Context.BIND_AUTO_CREATE);
		} else {
			context.startService(intent);
			context.bindService(intent, episodePlayerConnection,
					Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * Checks if the Episode Player Service is running or not
	 * 
	 * @return
	 */
	private boolean isEpisodePlayerRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
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
	 * Background thread that runs every second and sends duration updates to
	 * the PlayerInterface.
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	public static class DurationUpdaterThread extends Thread {

		private PlayerInterface playerInterface;
		private MediaPlayer mediaPlayer;
		private boolean interupt;

		public DurationUpdaterThread(MediaPlayer mediaPlayer,
				PlayerInterface playerInterface) {
			this.mediaPlayer = mediaPlayer;
			this.playerInterface = playerInterface;
		}
		
		public void interupt(){
			interupt = true;
		}

		@Override
		public void run() {
			while (!interupt) {
				try {
					Thread.sleep(1000);
					if (!mediaPlayer.isPlaying() && !paused) {
						interupt = true;
					}
					if (playerInterface != null) {
						playerInterface.durationUpdate(
								mediaPlayer.getDuration(),
								mediaPlayer.getCurrentPosition());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (playerInterface != null) {
				playerInterface.durationUpdate(0, 0);
			}
		}
	}


}
