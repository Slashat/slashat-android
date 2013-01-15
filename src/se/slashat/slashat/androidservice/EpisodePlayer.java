package se.slashat.slashat.androidservice;

import java.io.IOException;

import se.slashat.slashat.MainActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

/**
 * EpisodePlayerService that plays a selected episode in the background.
 * TODO: Wake locks, MediaPlayerState handling, seek in file, interaction with buttons.
 * 
 * @author nicklas.lof
 *
 */

public class EpisodePlayer extends Service implements OnPreparedListener {

	private MediaPlayer mediaPlayer;
	private EpisodePlayerBinder binder;
	private Notification notification;
	private String episodeName;

	public class EpisodePlayerBinder extends Binder {
		public EpisodePlayer getService() {
			return EpisodePlayer.this;
		}
	}

	public EpisodePlayer() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		binder = new EpisodePlayerBinder();
	}
	/**
	 * Starts a new Mediaplayer for the selected URL.
	 * @param streamUrl
	 * @param episodeName
	 */
	public void initializePlayer(String streamUrl, String episodeName) {
		this.episodeName = episodeName;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		if (streamUrl == null) {
			stopPlay(); // Temporary to stop playing until buttons are
						// implemented.
			return;
		}

		try {
			mediaPlayer.setDataSource(streamUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mediaPlayer.setOnPreparedListener(this);
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

	/**
	 * Start play the media set in initializePlayer() and plays the media in the background.
	 */
	private void startPlay() {
		//TODO Replace with Non deprecated way to create notifications
		notification = new Notification(
				android.R.drawable.ic_media_play, "slashat.se",
				System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		CharSequence contentTitle = "slashat.se spelar nu";
		CharSequence contentText = episodeName;

		notification.setLatestEventInfo(getApplicationContext(), contentTitle,
				contentText, pendingIntent);

		startForeground(1, notification);

		mediaPlayer.start();

	}
	/**
	 * Stops playing the current media and removes the player from the background
	 */
	public void stopPlay() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			stopForeground(true);
		}
	}

}
