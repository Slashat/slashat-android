package se.slashat.slashapp.async;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.util.IOUtils;
import se.slashat.slashapp.util.Strings;

/**
 * Created by nicklas on 10/24/13.
 */
public class HighFiveSetAsyncTask extends AsyncTask<String,Void,Boolean> {
    private Callback<Boolean> callback;

    public HighFiveSetAsyncTask(Callback<Boolean> callback) {

        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String urlString = strings[0];
        String receiver = strings[1];
        String longitude = strings[2];
        String latitude = strings[3];
        String token = strings[4];

        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);

            ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
            postData.add(new BasicNameValuePair("receiver",receiver));
            postData.add(new BasicNameValuePair("token",token));
            if (!Strings.isNullOrEmpty(longitude) && !Strings.isNullOrEmpty(latitude)){
                postData.add(new BasicNameValuePair("geolocation_lon", longitude));
                postData.add(new BasicNameValuePair("geolocation_lat", latitude));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(postData));
            HttpResponse response = defaultHttpClient.execute(httpPost);

            InputStream stream = response.getEntity().getContent();
            String userJsonString = IOUtils.readStringFromStream(stream);

            if (!Strings.isNullOrEmpty(userJsonString)){
                JSONObject jsonObject = new JSONObject(userJsonString);
                if (jsonObject.has("success")){
                    if (jsonObject.getBoolean("success")){
                        return true;
                    }
                }
            }

            return false;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        callback.call(result);
    }
}
