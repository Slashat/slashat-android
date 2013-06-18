package se.slashat.slashapp.async;

import java.util.ArrayList;

import se.slashat.slashapp.R;
import se.slashat.slashapp.adapter.EpisodeAdapter;
import se.slashat.slashapp.fragment.ArchiveListFragment;
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

	private ArchiveListFragment archiveFragment;
	private ProgressDialog progressDialog;
	private boolean fullRefresh;

	public EpisodeLoaderAsyncTask(ArchiveListFragment archiveFragment, boolean fullRefresh) {
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
		Episode[] episodes = ArchiveService.getEpisodes(new UpdateCallback() {

			@Override
			public void onUpdate() {
				publishProgress();
			}
		}, fullRefresh);

		return episodes;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(archiveFragment.getActivity());
		progressDialog.setTitle("Laddar avsnitt");
		progressDialog.setMessage("Laddar");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		progressDialog.setMessage(ArchiveService.getProgressMessage());
		int numberOfEpisodes = ArchiveService.getNumberOfEpisodes();
		if (progressDialog.getMax() != numberOfEpisodes) {
			progressDialog.setMax(numberOfEpisodes);
		}
		progressDialog.incrementProgressBy(ArchiveService.getProcessedNumbers() - progressDialog.getProgress());

	}

	@Override
	protected void onPostExecute(Episode[] result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		
		ArrayList<EpisodeViewModel> arrayList = new ArrayList<EpisodeViewModel>();
		for (int i = 0; i < result.length; i++) {
			arrayList.add(new EpisodeViewModel(result[i]));
		}
		
		archiveFragment.setListAdapter(new EpisodeAdapter(archiveFragment.getActivity(), R.layout.archive_item_row, arrayList.toArray(new EpisodeViewModel[arrayList.size()]), archiveFragment));
	}

}