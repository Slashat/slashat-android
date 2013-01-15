package se.slashat.slashat;


import se.slashat.slashat.fragment.AboutFragment;
import se.slashat.slashat.fragment.ArchiveFragment;
import se.slashat.slashat.fragment.FragmentSwitcher;
import se.slashat.slashat.fragment.LiveFragment;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar.Tab;

import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener,FragmentSwitcher {
    
	private static final String TAG = "Slashat";
	private static LiveFragment liveFrag;
	private static ArchiveFragment archiveFrag;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initiate our fragments
        if (savedInstanceState == null) {
            //initial fragment objects
        	liveFrag = new LiveFragment();
        	archiveFrag = new ArchiveFragment();
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
    
    public void switchFragment(Fragment fragment,boolean addToBackstack){
    	FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
    	beginTransaction.replace(android.R.id.content,fragment);
    	if (addToBackstack){
    		beginTransaction.addToBackStack(null);
    	}else{
    		getSupportFragmentManager().popBackStack();
    	}
    	beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	
    	beginTransaction.commit();
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//TODO: Rewrite this section to a fragment-container and call fragments based on their classnames and tab ids
				
		if (tab.getPosition() == 0) {
			//live		
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-live");
			switchFragment(liveFrag, false);
		} else if (tab.getPosition() == 1) {
			//archive			
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-archive");
			switchFragment(archiveFrag, false);
		} else if (tab.getPosition() == 2) {
			//about			
			Log.d(TAG, "Loading fragment for: " + tab.getPosition() + "-about");
			switchFragment(new AboutFragment(this), false); // Create a new AboutFragment each time.
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
