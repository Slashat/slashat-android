package se.slashat.slashapp.fragments.highfive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.model.highfive.HighFivedBy;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;

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


                //Call async when adding webservice
                User user = HighFiveService.getUser();

                view = inflater.inflate(R.layout.highfive_fragment, null);
                if (user != null) {

                    setText(view.findViewById(R.id.highfive_username), user.getName());
                    setText(view.findViewById(R.id.highfive_numberofhighfives), user.getHighFivers().size() + " highfives");
                    setText(view.findViewById(R.id.highfive_firsthighfive), formatFirstHighfivedBy(user.getHighFivedBy()));
                    setImage(view.findViewById(R.id.highfive_userimage), R.drawable.nicklas);
                }
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

    private String formatFirstHighfivedBy(HighFivedBy highFivedBy) {
        String base = "Fick sin första high-five av %s den %s i %s";
        return String.format(base, highFivedBy.getName(), highFivedBy.getDate(), highFivedBy.getLocation());
    }


    private void setText(View view, String text) {
        ((TextView) view).setText(text);
    }

    private void setImage(View view, int resource) {
        ((ImageView) view).setImageResource(resource);
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
