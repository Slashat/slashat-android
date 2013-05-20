package se.slashat.slashat.async;

import java.util.Collection;

import se.slashat.slashat.Callback;
import se.slashat.slashat.model.Episode;
import se.slashat.slashat.model.LiveEvent;
import se.slashat.slashat.service.CalendarService;
import android.os.AsyncTask;

public class CalendarLoaderAsyncTask extends AsyncTask<Void, Void, Collection<LiveEvent>>{

	private Callback<Collection<LiveEvent>> callback;

	public CalendarLoaderAsyncTask(Callback<Collection<LiveEvent>> callback) {
		this.callback = callback;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Collection<LiveEvent> doInBackground(Void... params) {
		Collection<LiveEvent> liveEvents = CalendarService.getLiveEvents();
		
		return liveEvents;
	}
	
	@Override
	protected void onPostExecute(Collection<LiveEvent> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.call(result);
		
	}
}
