package se.slashat.slashat.fragment;

import java.io.Serializable;

import se.slashat.slashat.CallbackPair;
import se.slashat.slashat.R;
import se.slashat.slashat.adapter.EpisodeDetailAdapter;
import se.slashat.slashat.adapter.PersonAdapter;
import se.slashat.slashat.adapter.PersonalAdapter;
import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.async.EpisodeLoaderAsyncTask;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.model.Personal;
import se.slashat.slashat.service.PersonalService;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ArchiveFragment extends ListFragment implements CallbackPair<Episode, Boolean>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String EPISODEPLAYER = "episodePlayer";
	public static final String ADAPTER = "adapter";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_archive, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = savedInstanceState == null ? getArguments()
				: savedInstanceState;

		@SuppressWarnings("unchecked")
		ArrayAdapter<Personal> adapter = (ArrayAdapter<Personal>) bundle.getSerializable(ADAPTER);
		
		// If no adapter is found in the bundle create a new one with all
		// people.
		if (adapter == null) {
			EpisodeLoaderAsyncTask episodeLoaderAsyncTask = new EpisodeLoaderAsyncTask(this);
			episodeLoaderAsyncTask.execute();
		}else{
		setListAdapter(adapter);
		}
		


	}

	/**
	 * Callback from the Adapter requesting an episode to be played.
	 */
	@Override
	public void call(Episode episode, Boolean showDetails) {
		if (showDetails) {
			EpisodeDetailAdapter p = new EpisodeDetailAdapter(getActivity(), R.layout.archive_item_details, new Episode[] { episode },this);

			Bundle bundle = new Bundle();
			bundle.putSerializable(ADAPTER, p);

			ArchiveFragment archiveFragment = new ArchiveFragment();
			archiveFragment.setArguments(bundle);
			FragmentSwitcher.getInstance().switchFragment(archiveFragment, true);
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