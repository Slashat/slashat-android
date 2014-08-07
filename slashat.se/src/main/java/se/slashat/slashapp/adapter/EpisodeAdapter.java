package se.slashat.slashapp.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import se.slashat.slashapp.CallbackPair;
import se.slashat.slashapp.R;
import se.slashat.slashapp.androidservice.EpisodePlayer;
import se.slashat.slashapp.model.Episode;
import se.slashat.slashapp.viewmodel.EpisodeViewModel;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EpisodeAdapter extends AbstractArrayAdapter<EpisodeViewModel> implements Serializable {
	/**
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CallbackPair<Episode, Boolean> episodeCallback;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("sv"));
	private PeriodFormatter durationFormatter = new PeriodFormatterBuilder()
    .appendHours()
    .appendSeparator(":")
    .appendMinutes()
    .appendSeparator(":")
    .appendSeconds()
    .toFormatter();

	public EpisodeAdapter(Context context, int layoutResourceId, EpisodeViewModel[] data, CallbackPair<Episode, Boolean> episodeCallback) {
		super(context, layoutResourceId, 0, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.episodeCallback = episodeCallback;
	}

	public static class EpisodeHolder extends Holder {
		TextView episodeNumber;
		TextView txtTitle;
		TextView date;
        TextView description;
        ImageView playButton;

	}

	@Override
	public Holder createHolder(View row) {
		EpisodeHolder holder = new EpisodeHolder();
		holder.episodeNumber = (TextView) row.findViewById(R.id.episodeNumber);
		holder.txtTitle = (TextView) row.findViewById(R.id.episodeTitle);
		holder.date = (TextView) row.findViewById(R.id.dateAndLength);
		holder.description = (TextView) row.findViewById(R.id.descriptionText);
        holder.playButton = (ImageView) row.findViewById(R.id.playbutton);
		return holder;
	}

	@Override
	public OnClickListener createOnClickListener(final EpisodeViewModel episodeViewModel) {
		return new OnClickListener() {
			/**
			 * Pass the selected episodeViewModel details to the Fragment that will start
			 * playing it.
			 */
			@Override
			public void onClick(View v) {
				episodeCallback.call(episodeViewModel.getEpisode(), true);
			}
		};
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void setDataOnHolder(Holder holder, final EpisodeViewModel episode, int position) {
		EpisodeHolder eh = (EpisodeHolder) holder;
		eh.episodeNumber.setText(episode.getFormatedEpisodeNumber());
		eh.txtTitle.setText(episode.getName());
        eh.description.setText(episode.getDescription());
		
		String duration = episode.getDuration();
		if (countOccurrences(duration, ':') == 1){
			duration = "00:"+duration; // For episodes shorter than 1 hour.
		}


        Period period = durationFormatter.parsePeriod(duration).normalizedStandard(PeriodType.minutes());
        String periodString = String.valueOf(period.getMinutes())+" minuter";
        eh.date.setText(dateFormat.format(episode.getPublished()) + " - " + periodString);

        eh.playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EpisodePlayer.getEpisodePlayer().playStream(episode.getEpisode().getStreamUrl(), episode.getEpisode().getFullEpisodeName(), 0, null, getContext());
            }
        });
	}
	
	public int countOccurrences(String string, char needle)
	{
	    int count = 0;
	    for (int i=0; i < string.length(); i++)
	    {
	        if (string.charAt(i) == needle)
	        {
	             count++;
	        }
	    }
	    return count;
	}
	
}
