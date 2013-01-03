package se.slashat.slashat;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar.Tab;

import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
    
	private static final String TAG = "Slashat";
	private static LiveFragment liveFrag;
	private static ArchiveFragment archiveFrag;
	private static AboutFragment aboutFrag;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //initiate our fragments
        if (savedInstanceState == null) {
            //initial fragment objects
        	liveFrag = new LiveFragment();
        	archiveFrag = new ArchiveFragment();
        	aboutFrag = new AboutFragment();
        }
        
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    	ActionBar.Tab liveTab= getSupportActionBar().newTab();
    	liveTab.setText("Live");
    	ActionBar.Tab archiveTab = getSupportActionBar().newTab();
    	archiveTab.setText("Showarkiv");
    	ActionBar.Tab aboutTab = getSupportActionBar().newTab();
    	aboutTab.setText("Om Slashat");
    	
    	liveTab.setTabListener(this);
    	archiveTab.setTabListener(this);
    	aboutTab.setTabListener(this);

    	getSupportActionBar().addTab(liveTab);
    	getSupportActionBar().addTab(archiveTab);
    	getSupportActionBar().addTab(aboutTab);
    	
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//TODO: Rewrite this section to a fragment-container and call fragments based on their classnames and tab ids
				
		if (tab.getPosition() == 0) {
			//live		
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-live");
			ft.replace(android.R.id.content, liveFrag);
		} else if (tab.getPosition() == 1) {
			//archive			
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-archive");
			ft.replace(android.R.id.content, archiveFrag);
		} else if (tab.getPosition() == 2) {
			//about			
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			ft.replace(android.R.id.content, aboutFrag);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}
	
}
