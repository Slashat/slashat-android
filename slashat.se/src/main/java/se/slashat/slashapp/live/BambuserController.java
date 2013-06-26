package se.slashat.slashapp.live;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static se.slashat.slashapp.Constants.BAMBUSER_TRANSCODE_API_KEY;
import static se.slashat.slashapp.Constants.BAMBUSER_TRANSCODE_URL;

/**
 * Created by nicklas on 6/26/13.
 */
public class BambuserController {

    private enum Preset {
        THREE_GP("3gp"),
        MP4_MEDIUM("mp4-medium"),
        HLS("hls");

        private final String presetString;

        Preset(String presetString) {
            this.presetString = presetString;
        }

        @Override
        public String toString() {
            return presetString;
        }
    }

    private final static String TRANSCODE_URL = BAMBUSER_TRANSCODE_URL + "%s.json?api_key=" + BAMBUSER_TRANSCODE_API_KEY + "&preset=%s";

    private URL buildTranscodeUrl(String broadcastId, Preset preset) throws MalformedURLException {

        return new URL(String.format(TRANSCODE_URL, broadcastId, preset.toString()));
    }

    public BambuserController() {
        try {
            URL url = buildTranscodeUrl("abc123", Preset.HLS);
            System.out.println(url.toExternalForm());
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getName(),"Can't create URL from string. Make sure Constants contains the correct information",e);
        }
    }
}
