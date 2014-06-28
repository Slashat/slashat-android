package se.slashat.slashapp.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.model.highfive.HighFivedBy;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.util.IOUtils;
import se.slashat.slashapp.util.Strings;

/**
 * Created by nicklas on 10/20/13.
 */
public class HighFiveGetUserAsyncTask extends AsyncTask<String, Void, User> {


    private Callback<User> callback;

    public HighFiveGetUserAsyncTask(Callback<User> callback) {
        this.callback = callback;
    }

    @Override
    protected User doInBackground(String... strings) {
        User user = null;

        try {
            String url = strings[0];
            String token = strings[1];


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();

            postData.add(new BasicNameValuePair("token", token));

            httppost.setEntity(new UrlEncodedFormEntity(postData));

            HttpResponse response = httpclient.execute(httppost);


            InputStream stream = response.getEntity().getContent();
            String userJsonString = IOUtils.readStringFromStream(stream);

            if (!Strings.isNullOrEmpty(userJsonString)) {
                JSONObject jsonObject = new JSONObject(userJsonString);
                user = getUserFromJson(jsonObject);
                if (user.getQr() != null){
                    InputStream qrStream = user.getQr().openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(qrStream);
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
                    user.setQrBitmap(blob.toByteArray());
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;

    }

    private User getUserFromJson(JSONObject jsonObject) throws JSONException, MalformedURLException {

        List<HighFiver> highFivers = new ArrayList<HighFiver>();
        String username = jsonObject.getString("username");
        String userId = jsonObject.getString("user_id");
        String pictureUrlString = jsonObject.getString("picture");
        String qrcodeString = jsonObject.getString("qrcode");
        //String qrcode_id = jsonObject.getString("qrcode_id");
        String qrcode_id = "";
        HighFivedBy highFivedBy = getHighFivedByFromJson(jsonObject);

        if (!(jsonObject.get("highfivers") instanceof JSONArray)){
            highFivers = getHighFiversFromJson(jsonObject.getJSONObject("highfivers"));
            Collections.sort(highFivers, new Comparator<HighFiver>() {
                @Override
                public int compare(HighFiver highFiver, HighFiver highFiver2) {
                    Long date1 = Long.valueOf(highFiver.getHighfivedDate());
                    Long date2 = Long.valueOf(highFiver2.getHighfivedDate());
                    return date2.compareTo(date1);
                }
            });
        }

        URL pictureUrl = null;
        URL qrcode = null;

        if (!Strings.isNullOrEmpty(pictureUrlString)){
            pictureUrl = new URL(pictureUrlString);
        }

        if (!Strings.isNullOrEmpty(qrcodeString)){
            qrcode = new URL(qrcodeString);
        }


        return new User(username,userId, highFivedBy, pictureUrl, qrcode, qrcode_id, highFivers);
    }

    private List<HighFiver> getHighFiversFromJson(JSONObject highfiversJson) throws JSONException, MalformedURLException {
        ArrayList<HighFiver> highFivers = new ArrayList<HighFiver>();
        if (highfiversJson == null){
            return highFivers;
        }
        Iterator keys = highfiversJson.keys();

        while(keys.hasNext()){
            String nextKey = (String) keys.next();
            JSONObject jsonObject = (JSONObject) highfiversJson.get(nextKey);
            String userName = jsonObject.getString("username");
            String pictureUrl = jsonObject.getString("picture");
            long highFivedDate = jsonObject.getLong("highfived_date");
            highFivers.add(new HighFiver(nextKey,userName,new URL(pictureUrl), highFivedDate));

        }

        return highFivers;
    }

    private HighFivedBy getHighFivedByFromJson(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("highfived_by_name");
        String date = jsonObject.getString("highfived_date");
        String where = jsonObject.getString("highfived_where");

        if (name.equals("null") || date.equals("null") || where.equals("null")){
            return new HighFivedBy("","","");
        }
        return new HighFivedBy(name,date, where);
    }

    @Override
    protected void onPostExecute(User user) {
        callback.call(user);
    }
}
