package se.slashat.slashapp.fragments.highfive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.AbstractArrayAdapter;
import se.slashat.slashapp.async.LoadImageAsyncTask;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.model.highfive.HighFivedBy;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;
import se.slashat.slashapp.service.ImageService;
import se.slashat.slashapp.util.ContentView;
import se.slashat.slashapp.util.Strings;

/**
 * Created by nicklas on 9/28/13.
 */
public class HighfiveFragment extends Fragment {
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            if (HighFiveService.hasToken()) {
                view = inflater.inflate(R.layout.highfive_fragment, null);

                populate(false);


            } else {
                view = inflater.inflate(R.layout.login, null);
                final EditText userNameEditText = (EditText) view.findViewById(R.id.loginusername);
                final EditText passwordEditText = (EditText) view.findViewById(R.id.loginpassword);
                Button loginButton = (Button) view.findViewById(R.id.loginbutton);
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String username = userNameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();


                        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                        if (deviceId == null) {
                            Toast toast = Toast.makeText(getActivity(), "Ledsen. Denna enhet stödjer inte möjligheten att extrahera ett unikt enhetsId", Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        HighFiveService.login(username, password, deviceId, new Callback<Boolean>() {
                                    // On Success
                                    @Override
                                    public void call(Boolean success) {
                                        if (success) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                            builder.setMessage("Inloggad. Klicka ok för att starta om appen!").setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Intent i = getActivity().getBaseContext().getPackageManager()
                                                                    .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                        }
                                                    });
                                            builder.create().show();

                                        }
                                    }
                                }, new Callback<String>() {
                                    // On Error

                                    // Please gief java 8 and lambda expression now....
                                    @Override
                                    public void call(String result) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                        builder.setMessage(result).setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                    }
                                                });
                                        builder.create().show();
                                    }
                                }
                        );

                    }
                });
            }

        } catch (InflateException e) {
        }
        return view;
    }

    private void populate(boolean reload) {
        HighFiveService.getUser(new Callback<User>() {
            @Override
            public void call(final User user) {
                if (user != null) {
                    UserImageHolder userImageHolder = new UserImageHolder();
                    userImageHolder.imageThumb = (ImageView) view.findViewById(R.id.highfive_userimage);
                    userImageHolder.image = (ImageView) view.findViewById(R.id.highfive_userimage);
                    userImageHolder.progressBar = (ProgressBar) view.findViewById(R.id.imageviewprogress);
                    userImageHolder.position = 0;
                    setText(view.findViewById(R.id.highfive_username), user.getUserName());
                    setText(view.findViewById(R.id.highfive_numberofhighfives), user.getHighFivers().size() + " highfives");
                    setText(view.findViewById(R.id.highfive_firsthighfive), formatFirstHighfivedBy(user.getHighFivedBy()));

                    if (user.getPicture() != null) {
                        ImageService.populateImage(userImageHolder, user.getPicture().toString(),0);
                    }

                    Button getHighfive = (Button) view.findViewById(R.id.gethighfive);
                    getHighfive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HighfiveFragment.this.getActivity(), RecieveHighFiveActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    });

                    Button giveHighfive = (Button) view.findViewById(R.id.givehighfive);
                    if (!user.getHighFivers().isEmpty()) {
                        giveHighfive.setEnabled(true);
                        giveHighfive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                IntentIntegrator.initiateScan(HighfiveFragment.this.getActivity());
                            }
                        });
                    }else{
                        giveHighfive.setEnabled(false);
                    }

                }
            }
        }, reload);
    }

    private String formatFirstHighfivedBy(HighFivedBy highFivedBy) {
        String base = "";

        if (Strings.isNullOrEmpty(highFivedBy.getDate()) || Strings.isNullOrEmpty(highFivedBy.getName())){
            return "Du har inte tagit emot din första high five ännu";
        }

        /*
        if (Strings.isNullOrEmpty(highFivedBy.getLocation())){
            base = "Fick sin första high-five av %s den %s";
        }else{
            base = "Fick sin första high-five av %s den %s i %s";
        }
        */
        base = "Fick sin första High-Five av %s den %s";

        DateTime dateTime = new DateTime(Long.valueOf(highFivedBy.getDate())*1000l, DateTimeZone.getDefault());
        String monthName = dateTime.monthOfYear().getAsText(new Locale("sv"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd '"+monthName+"', yyyy");
        String date = dateTimeFormatter.print(dateTime);

        return String.format(base, highFivedBy.getName(), date, highFivedBy.getLocation());
    }


    private void setText(View view, String text) {
        ((TextView) view).setText(text);
    }

    private void setImage(View view, int resource) {
        ((ImageView) view).setImageResource(resource);
    }

    public void reload() {
        populate(true);
    }

    public class UserImageHolder extends AbstractArrayAdapter.ImageAsyncHolder{

    }

    // TODO: Load async image
    private void setImage(View view, URL imageUrl) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
