package se.slashat.slashapp.fragments.highfive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.AbstractArrayAdapter;

/**
 * Created by nicklas on 6/29/14.
 */
public class BadgeDetailFragment extends Fragment {
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            String pictureLarge = getActivity().getIntent().getExtras().getString("pictureLarge");
            String description = getActivity().getIntent().getExtras().getString("description");


            view = inflater.inflate(R.layout.badge_detail, null);
            ImageView image = (ImageView) view.findViewById(R.id.badgeImage);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.imageviewprogress);
            TextView descriptionView = (TextView) view.findViewById(R.id.badgeDescription);


            final BadgeImageHolder badgeImageHolder = new BadgeImageHolder();
            badgeImageHolder.progressBar = progressBar;
            badgeImageHolder.image = image;
            badgeImageHolder.imageThumb = image;


            badgeImageHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(badgeImageHolder.imageThumb.getContext()).load(pictureLarge).into(badgeImageHolder.imageThumb, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    badgeImageHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    badgeImageHolder.progressBar.setVisibility(View.GONE);
                }
            });


            descriptionView.setText(description);


        } catch (InflateException e) {

        }

        return view;

    }

    public class BadgeImageHolder extends AbstractArrayAdapter.ImageAsyncHolder{

    }

}
