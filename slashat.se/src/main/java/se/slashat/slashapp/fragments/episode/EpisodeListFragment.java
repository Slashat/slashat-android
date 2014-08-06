package se.slashat.slashapp.fragments.episode;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import se.slashat.slashapp.CallbackPair;
import se.slashat.slashapp.R;
import se.slashat.slashapp.async.EpisodeLoaderAsyncTask;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.service.ArchiveService;

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
    private static EpisodeFragment callback;
    private SwipeRefreshLayout swipeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.episode_list, null);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.white,
                android.R.color.black,
                R.color.pressed_slashat,
                R.color.pressed_slashat);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate(true);
            }
        });
        swipeLayout.setRefreshing(true);
        return view;
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
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populate(false);
    }

    public void startProgress() {
        if (swipeLayout != null){
            swipeLayout.setRefreshing(true);
        }
    }

    public void stopProgress() {
        if (swipeLayout != null){
            swipeLayout.setRefreshing(false);
        }
    }

    public void populate(boolean fullRefresh) {
        //getActivity().setProgressBarIndeterminateVisibility(true);
        startProgress();
        EpisodeLoaderAsyncTask episodeLoaderAsyncTask = new EpisodeLoaderAsyncTask(this, fullRefresh);
        episodeLoaderAsyncTask.execute();
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        super.setListAdapter(adapter);
        //getActivity().setProgressBarIndeterminateVisibility(false);
        stopProgress();
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

        callback.call(result);
    }

    public static void setCallback(EpisodeFragment callback) {
        EpisodeListFragment.callback = callback;
    }
}
