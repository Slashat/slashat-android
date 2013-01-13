package se.slashat.slashat.adapter;

import se.slashat.slashat.model.Episode;
import se.slashat.slashat.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class EpisodeAdapter extends AbstractArrayAdapter<Episode> {


	public EpisodeAdapter(Context context, int layoutResourceId, Episode[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	public static class EpisodeHolder extends Holder{
		TextView episodeNumber;
		TextView txtTitle;
	}
	
	@Override
	
	public Holder createHolder(View row) {
		EpisodeHolder holder = new EpisodeHolder();
		holder.episodeNumber = (TextView) row.findViewById(R.id.episodeNumber);		
		holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
		return holder;
	}

	@Override
	public OnClickListener createOnClickListener(Episode e) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		};
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void setDataOnHolder(Holder holder, Episode episode) {
		EpisodeHolder eh = (EpisodeHolder) holder;
		eh.episodeNumber.setText("Avsnitt "+episode.getEpisodeNumber());
		eh.txtTitle.setText(episode.getName());

	}
}
