package se.slashat.slashapp.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.slashat.slashapp.Callback;

/**
 * Created by nicklas on 6/27/13.
 */
public class TranscodeLoaderAsyncTask extends AsyncTask<URL, Void, String> {

    private Callback<String> callback;

    public TranscodeLoaderAsyncTask(Callback<String> callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(URL... transcodeUrl) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) transcodeUrl[0].openConnection();

            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write("");
            out.close();

            String jsonString = readStringFromStream(connection.getInputStream());
            JSONObject jsonObject = new JSONObject(jsonString);

            String broadcastLink = jsonObject.getJSONObject("result").getString("url");

            return broadcastLink;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (connection.getResponseCode() > 200) {
                    Log.e(this.getClass().getName(), readStringFromStream(connection.getErrorStream()));
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String readStringFromStream(InputStream inputStream) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } finally {
            inputStream.close();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        callback.call(s);
    }
}
