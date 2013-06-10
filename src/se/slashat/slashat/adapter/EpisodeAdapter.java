package se.slashat.slashat.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import se.slashat.slashat.CallbackPair;
import se.slashat.slashat.R;
import se.slashat.slashat.model.Episode;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EpisodeAdapter extends AbstractArrayAdapter<Episode> implements Serializable {

	private static final long serialVersionUID = 1L;
	private CallbackPair<Episode, Boolean> episodeCallback;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("sv"));

	public EpisodeAdapter(Context context, int layoutResourceId, Episode[] data, CallbackPair<Episode, Boolean> episodeCallback) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.episodeCallback = episodeCallback;
	}

	public static class EpisodeHolder extends Holder {
		TextView episodeNumber;
		TextView txtTitle;

	}

	@Override
	public Holder createHolder(View row) {
		EpisodeHolder holder = new EpisodeHolder();
		holder.episodeNumber = (TextView) row.findViewById(R.id.episodeNumberAndTitle);
		holder.txtTitle = (TextView) row.findViewById(R.id.dateAndLength);
		return holder;
	}

	@Override
	public OnClickListener createOnClickListener(final Episode episode) {
		return new OnClickListener() {
			/**
			 * Pass the selected episode details to the Fragment that will start
			 * playing it.
			 */
			@Override
			public void onClick(View v) {
				// episodeCallback.call(episode.getStreamUrl(),episode.getFullEpisodeName());
				episodeCallback.call(episode, true);
			}
		};
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void setDataOnHolder(Holder holder, final Episode episode) {
		EpisodeHolder eh = (EpisodeHolder) holder;
		eh.episodeNumber.setText("#" + episode.getEpisodeNumber() + " - " + episode.getName());
		eh.txtTitle.setText(dateFormat.format(episode.getPublished()) + " - " + episode.getDuration());
	}
}
