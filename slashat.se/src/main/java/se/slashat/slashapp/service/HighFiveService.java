package se.slashat.slashapp.service;


import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.async.HighFiveGetUserAsyncTask;
import se.slashat.slashapp.async.HighFiveLoginAsyncTask;
import se.slashat.slashapp.async.HighFiveSetAsyncTask;
import se.slashat.slashapp.model.highfive.HighFivedBy;
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


    public static void initalize(Context context) {

        HighFiveService.context = context;
        getToken();
    }

    // Must be called async
    public static void getUser(Callback<User> callback) {
        boolean networkAvailable = Network.isNetworkAvailable();
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

    public static Collection<HighFiver> getAllHighfivers() {
        return Collections.EMPTY_LIST;
    }

    public static void setHighFive(String receiver, Callback<Boolean> callback){
        HighFiveSetAsyncTask highFiveSetAsyncTask = new HighFiveSetAsyncTask(callback);
        highFiveSetAsyncTask.execute(new String[]{DO,receiver,token});
    }

    private static void getUserFromAsyncTask(Callback<User> callback) {
        HighFiveGetUserAsyncTask highFiveGetUserAsyncTask = new HighFiveGetUserAsyncTask(callback);
        highFiveGetUserAsyncTask.execute(new String[]{GET_MY_HIGHFIVES,token});
    }
}
