package se.slashat.slashapp.async;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.util.IOUtils;
import se.slashat.slashapp.util.Strings;

/**
 * Created by nicklas on 10/20/13.
 */
public class HighFiveLoginAsyncTask extends AsyncTask<String,Void,String>{

    private Callback<String> callback;

    public HighFiveLoginAsyncTask(Callback<String> callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {

        String token = "";

        try {
            String url = strings[0];
            String username = strings[1];
            String password = strings[2];
            String deviceId = strings[3];

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();

            postData.add(new BasicNameValuePair("username",username));
            postData.add(new BasicNameValuePair("deviceid", deviceId));
            postData.add(new BasicNameValuePair("password", password));



            httppost.setEntity(new UrlEncodedFormEntity(postData));

            HttpResponse response = httpclient.execute(httppost);

            InputStream stream = response.getEntity().getContent();
            String tokenJsonString = IOUtils.readStringFromStream(stream);


            if (!Strings.isNullOrEmpty(tokenJsonString)){
                JSONObject jsonObject = new JSONObject(tokenJsonString);
                token = jsonObject.getString("token");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return token;

    }

    @Override
    protected void onPostExecute(String s) {
        callback.call(s);
    }
}
