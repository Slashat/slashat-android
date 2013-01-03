package se.slashat.slashat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class LiveShardFragment extends Fragment {
	
	private static final Boolean ForceLive = true;
	WebView mWebView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         *  Decide which layout to use - if we're on a Tuesday between 19.00 and 22.00 we show fragment_onlive
         *  other times we show fragment_live
         */
    	
    	Integer fragment;
    	
    	if (ForceLive == true) {
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
		
		//load bambuser (only load an image for now, bambuser will be a later challenge)
		//we should probably bounce this via the website, in case we change streaming-partner
		//eg http://slashat.se/mobile/livestream.php
		try {
			mWebView.loadUrl("http://slashat.se/wp-content/uploads/grafik/tshirtbanner.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}