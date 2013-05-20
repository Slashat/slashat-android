package se.slashat.slashat.async;

import se.slashat.slashat.R;
import se.slashat.slashat.adapter.EpisodeAdapter;
import se.slashat.slashat.fragment.ArchiveListFragment;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.service.ArchiveService;
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

	public EpisodeLoaderAsyncTask(ArchiveListFragment archiveFragment) {
		this.archiveFragment = archiveFragment;
	}

	public interface UpdateCallback {
		public void onUpdate();
	}
	
	public static UpdateCallback getVoidCallback(){
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
		});

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
		progressDialog.incrementProgressBy(ArchiveService.getProcessedNumbers()
				- progressDialog.getProgress());

	}

	@Override
	protected void onPostExecute(Episode[] result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		archiveFragment.setListAdapter(new EpisodeAdapter(archiveFragment
				.getActivity(), R.layout.archive_item_row, result,
				archiveFragment));
	}

}
