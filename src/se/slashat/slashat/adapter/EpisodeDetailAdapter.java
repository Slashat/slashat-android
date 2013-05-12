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

public class EpisodeDetailAdapter extends AbstractArrayAdapter<Episode> implements Serializable {

	private Episode episode;
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("sv"));
	private CallbackPair<Episode, Boolean> episodeCallback;
	
	
	public EpisodeDetailAdapter(Context context, int layoutResourceId, Episode[] data, CallbackPair<Episode,Boolean> episodeCallback) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.episodeCallback = episodeCallback;
		this.episode = data[0];
	}
	
	static class EpisodeDetailsHolder extends Holder {
		TextView episodeNumberAndTitle;
		TextView dateAndLength;
		TextView description;
		Button listenButton;
		Button viewButton;
	}

	@Override
	public AbstractArrayAdapter.Holder createHolder(View row) {
		EpisodeDetailsHolder holder = new EpisodeDetailsHolder();
		holder.episodeNumberAndTitle = (TextView) row.findViewById(R.id.episodeNumberAndTitle);
		holder.dateAndLength = (TextView) row.findViewById(R.id.dateAndLength);
		holder.description = (TextView) row.findViewById(R.id.description);
		holder.listenButton = (Button) row.findViewById(R.id.listenbutton);
		holder.viewButton = (Button) row.findViewById(R.id.viewbutton);
		return holder;
	}

	@Override
	public OnClickListener createOnClickListener(Episode t) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		};
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void setDataOnHolder(AbstractArrayAdapter.Holder holder, Episode t) {
		EpisodeDetailsHolder edh = (EpisodeDetailsHolder) holder;
		edh.episodeNumberAndTitle.setText("#"+episode.getEpisodeNumber()+" - "+episode.getName());
		edh.dateAndLength.setText(dateFormat.format(episode.getPublished())+" - "+episode.getDuration());
		edh.description.setText(t.getDescription());
		edh.listenButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				episodeCallback.call(episode, false);
			}
		});
		
		edh.viewButton.setEnabled(false);

	}
}
