package se.slashat.slashapp.fragments.episode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import se.slashat.slashapp.CallbackPair;
import se.slashat.slashapp.R;
import se.slashat.slashapp.async.EpisodeLoaderAsyncTask;
import se.slashat.slashapp.dummy.DummyContent;
import se.slashat.slashapp.model.Episode;

/**
 * Created by nicklas on 6/18/13.
 */
public class EpisodeListFragment extends ListFragment implements CallbackPair<Episode, Boolean> {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.episode_list,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().findViewById(R.layout.dualpanefinder) != null){
            mTwoPane = true;
        }
        populate(false);
    }


    public void populate(boolean fullRefresh) {
        EpisodeLoaderAsyncTask episodeLoaderAsyncTask = new EpisodeLoaderAsyncTask(this, fullRefresh);
        episodeLoaderAsyncTask.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void call(Episode result, Boolean pairResult) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("episode",result);

        EpisodeDetailFragment episodeDetailFragment = new EpisodeDetailFragment();
        episodeDetailFragment.setArguments(bundle);
        if (mTwoPane){
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.detailfragment, episodeDetailFragment).commit();
        }else{
            getFragmentManager().beginTransaction().addToBackStack(null).setCustomAnimations(R.anim.slider_in, R.anim.noanimation, R.anim.noanimation, R.anim.slider_out).replace(R.id.detailfragment, episodeDetailFragment).commit();
        }
    }
}
