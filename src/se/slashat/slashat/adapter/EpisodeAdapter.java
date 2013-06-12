package se.slashat.slashat.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import se.slashat.slashat.CallbackPair;
import se.slashat.slashat.R;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.viewmodel.EpisodeViewModel;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
		TextView length;

	}

	@Override
	public Holder createHolder(View row) {
		EpisodeHolder holder = new EpisodeHolder();
		holder.episodeNumber = (TextView) row.findViewById(R.id.episodeNumber);
		holder.txtTitle = (TextView) row.findViewById(R.id.episodeTitle);
		holder.date = (TextView) row.findViewById(R.id.dateAndLength);
		holder.length = (TextView) row.findViewById(R.id.length);
		return holder;
	}

	@Override
	public OnClickListener createOnClickListener(final EpisodeViewModel episode) {
		return new OnClickListener() {
			/**
			 * Pass the selected episode details to the Fragment that will start
			 * playing it.
			 */
			@Override
			public void onClick(View v) {
				// episodeCallback.call(episode.getStreamUrl(),episode.getFullEpisodeName());
				episodeCallback.call(episode.getModel(), true);
			}
		};
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void setDataOnHolder(Holder holder, final EpisodeViewModel episode) {
		EpisodeHolder eh = (EpisodeHolder) holder;
		eh.episodeNumber.setText("# "+ episode.getModel().getEpisodeNumber());
		eh.txtTitle.setText(episode.getModel().getName());
		eh.date.setText(dateFormat.format(episode.getModel().getPublished()));
		
		String duration = episode.getModel().getDuration();
		if (countOccurrences(duration, ':') == 1){
			duration = "00:"+duration; // For episodes shorter than 1 hour.
		}
		
		
		Period period = durationFormatter.parsePeriod(duration).normalizedStandard(PeriodType.minutes());
		String periodString = String.valueOf(period.getMinutes())+"m";
		eh.length.setText(periodString);

		
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
