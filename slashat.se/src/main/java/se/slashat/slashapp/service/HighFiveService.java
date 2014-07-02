package se.slashat.slashapp.service;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.async.HighFiveGetUserAsyncTask;
import se.slashat.slashapp.async.HighFiveLoginAsyncTask;
import se.slashat.slashapp.async.HighFiveSetAsyncTask;
import se.slashat.slashapp.async.HighFiversAllAsyncTask;
import se.slashat.slashapp.model.highfive.HighFiver;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.util.IOUtils;
import se.slashat.slashapp.util.Network;
import se.slashat.slashapp.util.Strings;

import static se.slashat.slashapp.Constants.HIGHFIVE_ALL;
import static se.slashat.slashapp.Constants.HIGHFIVE_BASE_URL;
import static se.slashat.slashapp.Constants.HIGHFIVE_DO;
import static se.slashat.slashapp.Constants.HIGHFIVE_GET_MY_HIGHFIVES;
import static se.slashat.slashapp.Constants.HIGHFIVE_LOGIN_TO_SERVICE;
import static se.slashat.slashapp.Constants.HIGHFIVE_REGISTER_TO_SERVICE;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighFiveService {

    private final static String LOGIN_TO_SERVICE = HIGHFIVE_BASE_URL + HIGHFIVE_LOGIN_TO_SERVICE;

    private final static String REGISTER_TO_SERVICE = HIGHFIVE_BASE_URL + HIGHFIVE_REGISTER_TO_SERVICE;

    private final static String GET_MY_HIGHFIVES = HIGHFIVE_BASE_URL + HIGHFIVE_GET_MY_HIGHFIVES;

    private final static String ALL = HIGHFIVE_BASE_URL + HIGHFIVE_ALL;

    private final static String DO = HIGHFIVE_BASE_URL + HIGHFIVE_DO;
    private static final String TOKENFILE = "token";
    private static Context context;
    private static String token;
    private static User user;
    private static List<HighFiver> highFivers;


    public static void initalize(Context context) {

        HighFiveService.context = context;
        getToken();
    }

    public synchronized static void getUser(Callback<User> callback, boolean reload) {

        if (user != null && !reload){
            callback.call(user);
            return;
        }
        boolean networkAvailable = Network.isNetworkAvailable();

        if (!networkAvailable && user != null){
            callback.call(user);
            return;
        }

        if (networkAvailable) {
            getUserFromAsyncTask(callback);
        }
    }

    public static void login(String username, String password, String deviceId, final Callback<Boolean> onSuccess, final Callback<String> onError) {
        if (token != null) {
            Log.w("HighfiveService", "Already logged in");
            return;
        }
        HighFiveLoginAsyncTask highFiveLoginAsyncTask = new HighFiveLoginAsyncTask(new Callback<String>() {
            @Override
            public void call(String result) {
                if (Strings.isNullOrEmpty(result)) {
                    onError.call("Kunde inte logga in");  // Fetch better error messages.
                } else {
                    storeToken(result);
                    onSuccess.call(true);
                }
            }
        });

        highFiveLoginAsyncTask.execute(new String[]{LOGIN_TO_SERVICE, username, password, deviceId});

    }

    private static void storeToken(String result) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(TOKENFILE, Context.MODE_PRIVATE);
            fileOutputStream.write(result.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getToken() {
        try {
            FileInputStream fileInputStream = context.openFileInput(TOKENFILE);
            token = IOUtils.readStringFromStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasToken() {
        return token != null;
    }

    public static void getAllHighfivers(Callback<List<HighFiver>> callback, boolean reload) {
        if (highFivers != null && !reload){
            callback.call(highFivers);
            return;
        }
        boolean networkAvailable = Network.isNetworkAvailable();

        if (!networkAvailable && highFivers != null){
            callback.call(highFivers);
            return;
        }

        if (networkAvailable) {
            getAllHighfiversFromAsyncTask(callback);
        }
    }

    public static void setHighFive(String receiver, Callback<Boolean> callback){
        String longitude = "";
        String latitude = "";
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null){
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
        }
        HighFiveSetAsyncTask highFiveSetAsyncTask = new HighFiveSetAsyncTask(callback);
        highFiveSetAsyncTask.execute(new String[]{DO,receiver,longitude,latitude,token});
    }

    private static void getAllHighfiversFromAsyncTask(final Callback<List<HighFiver>> callback){
        HighFiversAllAsyncTask highFiversAllAsyncTask = new HighFiversAllAsyncTask(new Callback<List<HighFiver>>() {
            @Override
            public void call(List<HighFiver> result) {
                highFivers = result;

                callback.call(result);
            }
        });
        highFiversAllAsyncTask.execute(ALL);

    }

    private static void getUserFromAsyncTask(final Callback<User> callback) {
        HighFiveGetUserAsyncTask highFiveGetUserAsyncTask = new HighFiveGetUserAsyncTask(new Callback<User>() {


            @Override
            public void call(User result) {
                user = result;
                callback.call(result);
            }
        });
        highFiveGetUserAsyncTask.execute(new String[]{GET_MY_HIGHFIVES,token});
    }
}
