package se.slashat.slashapp.fragments.episode;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.slashat.slashapp.Callback;
import se.slashat.slashapp.R;
import se.slashat.slashapp.fragments.FragmentSwitcher;
import se.slashat.slashapp.model.Episode;

/**
 * Created by nicklas on 6/18/13.
 */
public class EpisodeFragment extends Fragment implements Callback<Episode> {
    private static View view;
    private boolean isDualPane;
    private EpisodeListFragment episodeListFragment;
    private EpisodeDetailFragment episodeDetailFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.episode_fragment, null);
        } catch (InflateException e) {

        }

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
        episodeListFragment = (EpisodeListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.listfragment);
        episodeDetailFragment = (EpisodeDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detailfragment);

        View detailView = view.findViewById(R.id.detailfragment);

        isDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;

        episodeListFragment.setCallback(this);
    }

    @Override
    public void call(Episode result) {

        if (isDualPane){
            Bundle bundle = new Bundle();
            bundle.putSerializable("episode", result);

            episodeDetailFragment = new EpisodeDetailFragment();
            episodeDetailFragment.setArguments(bundle);

            FragmentSwitcher.getInstance().switchFragment(episodeDetailFragment, true, R.id.detailfragment);
        }else{
            Intent intent = new Intent(this.getActivity(), EpisodeDetailActivity.class);
            intent.putExtra("episode", result);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
        }

    }


}
