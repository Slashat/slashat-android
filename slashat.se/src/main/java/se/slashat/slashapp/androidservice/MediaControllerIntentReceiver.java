package se.slashat.slashapp.androidservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by nicklas on 7/9/14.
 */
public class MediaControllerIntentReceiver extends BroadcastReceiver {
    public MediaControllerIntentReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getName(), " MediaControllerIntentReciever called " + intent);

        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                togglePlay();
            }
        } else if (intent.getAction().equals(EpisodePlayer.PLAYPAUSE_COMMAND)) {
            togglePlay();
        }
    }

    private void togglePlay() {
        EpisodePlayer episodePlayer = EpisodePlayer.getEpisodePlayer();
        if (episodePlayer.isPlaying()) {
            episodePlayer.pause();
        } else {
            episodePlayer.resume();
        }
    }
}
