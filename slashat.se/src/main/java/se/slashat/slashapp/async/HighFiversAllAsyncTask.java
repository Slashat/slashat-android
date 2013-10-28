package se.slashat.slashapp.async;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.util.IOUtils;

/**
 * Created by nicklas on 10/28/13.
 */
public class HighFiversAllAsyncTask extends AsyncTask<String, Void, Collection<HighFiver>> {

    private Callback<Collection<HighFiver>> callback;

    public HighFiversAllAsyncTask(Callback<Collection<HighFiver>> callback) {
        this.callback = callback;
    }

    @Override
    protected Collection<HighFiver> doInBackground(String... strings) {
        Collection<HighFiver> highFivers = new ArrayList<HighFiver>();

        HttpURLConnection connection = null;

        try {
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();

            String jsonString = IOUtils.readStringFromStream(connection.getInputStream());
            JSONObject jsonObject = new JSONObject(jsonString);


            Iterator keys = jsonObject.keys();

            while(keys.hasNext()){
                String nextKey = (String) keys.next();
                JSONObject userJsonObject = jsonObject.getJSONObject(nextKey);
                String username = userJsonObject.getString("username");
                String userId = userJsonObject.getString("user_id");
                String picture = userJsonObject.getString("picture");


                HighFiver highFiver = new HighFiver(userId, username, new URL(picture));
                highFivers.add(highFiver);
            }


        } catch (MalformedURLException ex) {

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highFivers;

    }

    @Override
    protected void onPostExecute(Collection<HighFiver> highFivers) {
        callback.call(highFivers);
    }
}
