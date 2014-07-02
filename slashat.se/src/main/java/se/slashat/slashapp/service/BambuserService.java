package se.slashat.slashapp.service;

import android.os.Build;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.async.LiveStreamLoaderAsyncTask;
import se.slashat.slashapp.async.TranscodeLoaderAsyncTask;
import se.slashat.slashapp.util.Strings;

import static se.slashat.slashapp.Constants.BAMBUSER_TRANSCODE_API_KEY;
import static se.slashat.slashapp.Constants.BAMBUSER_TRANSCODE_URL;

/**
 * Created by nicklas on 6/26/13.
 */
public class BambuserService {

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
    private final static String LIVESTREAMS_URL = "http://api.bambuser.com/broadcast.json?username=slashat&type=live&limit=1&api_key="+BAMBUSER_TRANSCODE_API_KEY;

    private static URL buildTranscodeUrl(String broadcastId, Preset preset){
        try{
            return new URL(String.format(TRANSCODE_URL, broadcastId, preset.toString()));
        } catch (MalformedURLException e) {
            Log.e("BambuserService", "Can't create URL from string. Make sure Constants contains the correct information", e);
            return null;
        }
    }

    public static void startStream(String broadCastId, final Callback<String> callback) {
        try {
            if (Strings.isNullOrEmpty(broadCastId)){
                getLiveStream(new Callback<String>() {
                    @Override
                    public void call(String result) {
                        try {
                            loadTranscodedUrl(result, callback);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else{
                loadTranscodedUrl(broadCastId, callback);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void loadTranscodedUrl(String broadCastId, Callback<String> callback) throws MalformedURLException {
        URL url = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !android.os.Build.MANUFACTURER.toLowerCase().contains("samsung")){
            url = buildTranscodeUrl(broadCastId, Preset.HLS);
        }else{
            url = buildTranscodeUrl(broadCastId, Preset.MP4_MEDIUM);
        }
        TranscodeLoaderAsyncTask transcodeLoaderAsyncTask = new TranscodeLoaderAsyncTask(callback);
        transcodeLoaderAsyncTask.execute(url);
    }

    public static void getLiveStream(Callback<String> callback){
        LiveStreamLoaderAsyncTask liveStreamLoaderAsyncTask = new LiveStreamLoaderAsyncTask(callback);
        try {
            liveStreamLoaderAsyncTask.execute(new URL(LIVESTREAMS_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
