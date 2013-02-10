package se.slashat.slashat.fragment;

import se.slashat.slashat.CallbackPair;
import se.slashat.slashat.R;
import se.slashat.slashat.adapter.EpisodeAdapter;
import se.slashat.slashat.androidservice.EpisodePlayer;
import se.slashat.slashat.service.ArchiveService;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArchiveFragment extends ListFragment implements
		CallbackPair<String, String> {

	public final static String EPISODEPLAYER = "episodePlayer";
	private EpisodeAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_archive, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new EpisodeAdapter(getActivity(), R.layout.archive_item_row,
				ArchiveService.getEpisodes(), this);
		setListAdapter(adapter);
	}

	/**
	 * Callback from the Adapter requesting an episode to be played.
	 */
	@Override
	public void call(String streamUrl, String episodeName) {
		EpisodePlayer.getEpisodePlayer().stopPlay();
		EpisodePlayer.getEpisodePlayer().initializePlayer(streamUrl,
				episodeName);

	}

}