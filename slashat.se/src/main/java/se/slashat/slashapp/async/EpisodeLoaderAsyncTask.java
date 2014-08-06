package se.slashat.slashapp.async;

import java.util.ArrayList;

import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.EpisodeAdapter;
import se.slashat.slashapp.fragments.episode.EpisodeListFragment;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.service.ArchiveService;
import se.slashat.slashapp.viewmodel.EpisodeViewModel;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Load episodes from the EpisodeService async with ProgressDialog updates
 * 
 * @author Nicklas Löf
 * 
 */
public class EpisodeLoaderAsyncTask extends AsyncTask<Void, Void, Episode[]> {

	private EpisodeListFragment archiveFragment;
	private boolean fullRefresh;

	public EpisodeLoaderAsyncTask(EpisodeListFragment archiveFragment, boolean fullRefresh) {
		this.archiveFragment = archiveFragment;
		this.fullRefresh = fullRefresh;
	}

	public interface UpdateCallback {
		public void onUpdate();
	}

	public static UpdateCallback getVoidCallback() {
		return new UpdateCallback() {

			@Override
			public void onUpdate() {

			}
		};
	}

	@Override
	protected Episode[] doInBackground(Void... params) {
		Episode[] episodes = ArchiveService.getInstance().getEpisodes(new UpdateCallback() {

			@Override
			public void onUpdate() {
				publishProgress();
			}
		}, fullRefresh);

		return episodes;
	}


	@Override
	protected void onPostExecute(Episode[] result) {
		super.onPostExecute(result);
		
		ArrayList<EpisodeViewModel> arrayList = new ArrayList<EpisodeViewModel>();
		for (int i = 0; i < result.length; i++) {
			arrayList.add(new EpisodeViewModel(result[i]));
		}

		archiveFragment.setListAdapter(new EpisodeAdapter(archiveFragment.getActivity(), R.layout.episode_list_item_row, arrayList.toArray(new EpisodeViewModel[arrayList.size()]), archiveFragment));
	}

}
