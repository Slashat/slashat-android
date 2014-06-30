package se.slashat.slashapp.player;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import se.slashat.slashapp.R;
import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.async.EpisodeLoaderAsyncTask;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.service.ArchiveService;
import se.slashat.slashapp.util.Conversion;
import se.slashat.slashapp.util.Network;

/**
 * Created by nicklas on 6/24/13.
 */
public class PlayerInterfaceImpl implements EpisodePlayer.PlayerInterface, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private SeekBar seekBar; //= (SeekBar) findViewById(R.id.seekbar)
    private ImageView button; //= (ImageButton) findViewById(R.id.playPauseButton)
    private int newPosition;
    private Dialog seekbarOverlay;
    private boolean seekbarOverlayShowing = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private TextView textView;
    private Activity callingActivity;

    public PlayerInterfaceImpl(Activity callingActivity) {
        this.callingActivity = callingActivity;
        seekBar = (SeekBar) callingActivity.findViewById(R.id.seekbar);
        button = (ImageView) callingActivity.findViewById(R.id.playPauseButton);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        seekBar.setOnSeekBarChangeListener(this);
        button.setOnClickListener(this);
        seekBar.setEnabled(false);
    }

    @Override
    public void durationUpdate(final int seekMax, final int seek) {
        if (seekbarOverlayShowing) {
            return;
        }
        callingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setEnabled(true);
                seekBar.setMax(seekMax);
                seekBar.setProgress(seek);
                onMediaPlaying("");
                TextView durationTextView = (TextView) callingActivity.findViewById(R.id.durationTextView);
                if (durationTextView != null) {
                    durationTextView.setText(dateFormat.format(new Date(seek)));
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBar.setEnabled(true);
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
        seekbarOverlay = new Dialog(callingActivity);
        seekbarOverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
        seekbarOverlay.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        seekbarOverlay.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams overlayParams = seekbarOverlay.getWindow().getAttributes();
        overlayParams.gravity = Gravity.TOP | Gravity.LEFT;
        overlayParams.x = (int) (seekBar.getLeft() + ((float) seekBar.getProgress() / (float) seekBar.getMax()) * seekBar.getWidth());
        int[] location = new int[2];
        seekBar.getLocationOnScreen(location);
        overlayParams.y = location[1] - Conversion.getPx(70);
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
        button.setImageResource(R.drawable.ic_media_play);
        // maybe use setDrawable instead
    }

    @Override
    public void onMediaStopped(String episodeName, boolean EOF) {
        seekBar.setEnabled(false);
        button.setImageResource(R.drawable.ic_media_play);
        // maybe use setDrawable instead
        Log.i("PlayerInterfaceImpl", "Current: " + episodeName);
        if (EOF && episodeName != null && !episodeName.equals("") && Network.isNetworkAvailable()) {
            List<Episode> episodes = Arrays.asList(ArchiveService.getInstance().getEpisodes(EpisodeLoaderAsyncTask.getVoidCallback(), false));

            Episode newEpisode = null;
            EPISODELOOP:
            for (Iterator<Episode> iterator = episodes.iterator(); iterator.hasNext(); ) {
                Episode e = iterator.next();
                String fullName = e.getFullEpisodeName();
                if (fullName.equals(episodeName)) {
                    newEpisode = iterator.next();
                    break EPISODELOOP;
                }

            }

            if (newEpisode != null) {
                EpisodePlayer.getEpisodePlayer().playStream(newEpisode.getStreamUrl(), newEpisode.getFullEpisodeName(), 0, null, callingActivity);
            }
        }

    }

    @Override
    public void onMediaPlaying(String episodeName) {
        button.setImageResource(R.drawable.ic_media_pause);
        seekBar.setEnabled(true);
        // maybe use setDrawable instead
    }

    @Override
    public void onClick(View v) {
        if (!EpisodePlayer.getEpisodePlayer().isMediaInitalized()) {

            String lastPlayedEpisodeName = EpisodePlayer.getEpisodePlayer().getLastPlayedEpisodeName();
            int lastPlayedPosition = EpisodePlayer.getEpisodePlayer().getLastPlayedPosition();
            String lastPlayedStreamUrl = EpisodePlayer.getEpisodePlayer().getLastPlayedStreamUrl();
            if (lastPlayedStreamUrl != null && !lastPlayedStreamUrl.equals("")) {
                EpisodePlayer.getEpisodePlayer().playStream(lastPlayedStreamUrl, lastPlayedEpisodeName, lastPlayedPosition, null, callingActivity);
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
        WindowManager.LayoutParams overlayParams = seekbarOverlay.getWindow().getAttributes();
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