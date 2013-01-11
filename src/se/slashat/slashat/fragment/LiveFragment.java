package se.slashat.slashat.fragment;

import se.slashat.slashat.R;
import se.slashat.slashat.R.id;
import se.slashat.slashat.R.layout;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class LiveFragment extends Fragment {
	
	private static final Boolean ForceLive = true;
	private static final String TAG = "Slashat-LiveFragment";
	WebView mWebView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         *  Decide which layout to use - if we're on a Tuesday between 19.00 and 22.00 we show fragment_onlive
         *  other times we show fragment_live
         */
    	
    	Integer fragment;
    	
    	if (ForceLive) { //or matches time, to be added
    		fragment = R.layout.fragment_livewindow_onair;
    	} else {
    		fragment = R.layout.fragment_livewindow_offair;
    	}
    	
    	return inflater.inflate(fragment, container, false);
    }
    
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWebView = (WebView) getView().findViewById(R.id.webView1);
		
		//load our webclient
        mWebView.setWebViewClient(new SlashatWebViewClient());
        
        //settings
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        
		//load bambuser
		//we should probably bounce this via the website, in case we change streaming-partner
		//eg http://slashat.se/mobile/livestream.php
		try {
			mWebView.loadUrl("http://embed.bambuser.com/channel/slashat?autoplay=1&chat=0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*
	 * 
	 * We need this class to override the default webbrowser behavior in Android.
	 * 
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