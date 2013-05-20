package se.slashat.slashat.fragment;

import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import se.slashat.slashat.Callback;
import se.slashat.slashat.R;
import se.slashat.slashat.async.CalendarLoaderAsyncTask;
import se.slashat.slashat.model.LiveEvent;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class LiveFragment extends Fragment implements Callback<Collection<LiveEvent>> {

	private static final int BUTTON_TIMEOUT = 3;
	private static final Boolean ForceLive = false;
	private static final String TAG = "Slashat-LiveFragment";
	private CountDownTimer countDownTimer;
	WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_livewindow_offair, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		CalendarLoaderAsyncTask calendarLoaderAsyncTask = new CalendarLoaderAsyncTask(this);
		calendarLoaderAsyncTask.execute();

		/**
		 * final Button moveonButton = (Button) getView().findViewById(
		 * R.id.moveonbutton);
		 * 
		 * moveonButton.setOnClickListener(new
		 * MoveonButtonListener(moveonButton));
		 * 
		 * mWebView = (WebView) getView().findViewById(R.id.webView1);
		 * 
		 * // load our webclient mWebView.setWebViewClient(new
		 * SlashatWebViewClient());
		 * 
		 * // settings mWebView.getSettings().setJavaScriptEnabled(true);
		 * mWebView.getSettings().setPluginsEnabled(true);
		 * mWebView.getSettings().setDomStorageEnabled(true);
		 * 
		 * // load bambuser // we should probably bounce this via the website,
		 * in case we change // streaming-partner // eg
		 * http://slashat.se/mobile/livestream.php try { mWebView.loadUrl(
		 * "http://embed.bambuser.com/channel/slashat?autoplay=1&chat=0"); }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 **/

	}

	@Override
	public void call(Collection<LiveEvent> result) {

		Date now = new Date();
		LIVEEVENTLOOP: for (LiveEvent liveEvent : result) {

			if (liveEvent.getStart().getTime() > now.getTime()) {
				startLiveCountDown(liveEvent, now, liveEvent.getStart());
				final TextView summaryTextView = (TextView) getView().findViewById(R.id.counterSummary);
				summaryTextView.setText(liveEvent.getSummary());
				break LIVEEVENTLOOP;
			}
		}
	}

	private void startLiveCountDown(final LiveEvent liveEvent, final Date now, final Date start) {
		final TextView textView = (TextView) getView().findViewById(R.id.counterTextView);
		final PeriodFormatter formatter = 
				new PeriodFormatterBuilder()
				.appendDays()
				.appendSeparator(" dagar och ")
				.printZeroAlways()
				.minimumPrintedDigits(2)
				.appendHours()
				.appendSeparator(":")
				.appendMinutes()
				.appendSeparator(":")
				.appendSeconds()
				.toFormatter();
		
		countDownTimer = new CountDownTimer(start.getTime() - now.getTime(), 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				DateTime now = new DateTime();
				DateTime nextLive = new DateTime(start);
				Period period = new Period(now, nextLive);


				textView.setText(formatter.print(period.normalizedStandard()));
			}

			@Override
			public void onFinish() {

			}
		};
		countDownTimer.start();

	}

	private final class MoveonButtonListener implements OnClickListener {
		private final Button moveonButton;
		private CountDownTimer countDownTimer;

		private MoveonButtonListener(Button moveonButton) {
			this.moveonButton = moveonButton;
		}

		@Override
		public void onClick(View v) {
			// Send click to slashat server.
			// Url.connectblahblahblh();

			// when recieved http 200 reply:

			final CharSequence currentText = moveonButton.getText();
			moveonButton.setText("Move on skickat!");
			moveonButton.setEnabled(false);

			// Countdowntimer to prevent button being spam clicked.
			countDownTimer = new CountDownTimer(BUTTON_TIMEOUT * 60 * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					long timeLeftInSeconds = millisUntilFinished / 1000;
					moveonButton.setText("Move on skickat! (" + timeLeftInSeconds + " sekunder till nästa)");
				}

				@Override
				public void onFinish() {
					moveonButton.setEnabled(true);
					moveonButton.setText(currentText);
				}
			};
			countDownTimer.start();
		}
	}

	/*
	 * 
	 * We need this class to override the default webbrowser behavior in
	 * Android.
	 */
	private class SlashatWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Browser Loading: " + url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Log.d(TAG, "Browser Error: " + errorCode + " - " + description);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

		}

		@Override
		public void onPageFinished(WebView view, String url) {

		}
	}

}