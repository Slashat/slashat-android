package se.slashat.slashat.fragment;

import java.io.Serializable;

import se.slashat.slashat.CallbackPair;
import se.slashat.slashat.R;
import se.slashat.slashat.adapter.EpisodeDetailAdapter;
import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.async.EpisodeLoaderAsyncTask;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.viewmodel.EpisodeViewModel;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;

public class ArchiveListFragment extends SherlockListFragment implements CallbackPair<Episode, Boolean>, Serializable {

	/**
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String EPISODEPLAYER = "episodePlayer";
	public static final String ADAPTER = "adapter";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		setHasOptionsMenu(true);
		setMenuVisibility(true);
		return inflater.inflate(R.layout.fragment_archivelist, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.reload_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload:
			populate(true);
		}

		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;

		ArrayAdapter<Episode> adapter = null;

		if (bundle != null) {
			adapter = (ArrayAdapter<Episode>) bundle.getSerializable(ADAPTER);
		}

		// If no adapter is found in the bundle create a new one with all
		// episodes.
		if (adapter == null) {
			populate(false);
		} else {
			setListAdapter(adapter);
		}

		setHasOptionsMenu(true);

	}

	public void populate(boolean fullRefresh) {
		EpisodeLoaderAsyncTask episodeLoaderAsyncTask = new EpisodeLoaderAsyncTask(this, fullRefresh);
		episodeLoaderAsyncTask.execute();
	}

	/**
	 * Callback from the Adapter requesting an episode to be played.
	 */
	@Override
	public void call(Episode episode, Boolean showDetails) {
		if (showDetails) {
			EpisodeDetailAdapter p = new EpisodeDetailAdapter(getActivity(), R.layout.archive_item_details, new EpisodeViewModel[] { new EpisodeViewModel(episode) }, this);

			Bundle bundle = new Bundle();
			bundle.putSerializable(ADAPTER, p);

			ArchiveDetailFragment archiveFragment = new ArchiveDetailFragment();
			archiveFragment.setArguments(bundle);
			FragmentSwitcher.getInstance().switchFragment(archiveFragment, true, R.id.detailFragment);
		} else {
			ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Buffrar avsnitt");
			progressDialog.setMessage(episode.getFullEpisodeName());
			progressDialog.show();
			EpisodePlayer.getEpisodePlayer().stopPlay();
			EpisodePlayer.getEpisodePlayer().initializePlayer(episode.getStreamUrl(), episode.getFullEpisodeName(), 0, progressDialog);

		}
	}

}