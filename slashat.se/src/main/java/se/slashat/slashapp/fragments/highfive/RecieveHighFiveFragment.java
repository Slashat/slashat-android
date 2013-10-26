package se.slashat.slashapp.fragments.highfive;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.model.highfive.User;
import se.slashat.slashapp.service.HighFiveService;

/**
 * Created by nicklas on 10/21/13.
 */
public class RecieveHighFiveFragment extends Fragment {
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(R.layout.recievehighfive, null);


            HighFiveService.getUser(new Callback<User>() {
                @Override
                public void call(User user) {
                    if (user != null) {
                        ImageView qr = (ImageView) view.findViewById(R.id.qrcode);
                        qr.setImageBitmap(BitmapFactory.decodeByteArray(user.getQrBitmap(),0,user.getQrBitmap().length));
                    }
                }
            },false);


        } catch (InflateException e) {

        }

        return view;

    }

}
