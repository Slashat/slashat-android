package se.slashat.slashat;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar.Tab;

import android.widget.Toast;

public class LiveActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    	ActionBar.Tab tabLive= getSupportActionBar().newTab();
    	tabLive.setText("Live");
    	ActionBar.Tab newTab1 = getSupportActionBar().newTab();
    	newTab1.setText("Arkiv");
    	ActionBar.Tab newTab2 = getSupportActionBar().newTab();
    	newTab2.setText("Om Oss");
    	
    	tabLive.setTabListener(this);
    	newTab1.setTabListener(this);
    	newTab2.setTabListener(this);

    	getSupportActionBar().addTab(tabLive);
    	getSupportActionBar().addTab(newTab1);
    	getSupportActionBar().addTab(newTab2);
    	
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		this.toastText(tab.getText().toString());
		
		LiveFragment frag = new LiveFragment();
		ft.replace(android.R.id.content, frag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	private void toastText(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	

}
