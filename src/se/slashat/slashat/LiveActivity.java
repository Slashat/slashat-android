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

    	ActionBar.Tab newTab0 = getSupportActionBar().newTab();
    	newTab0.setText("Tab 0 title");
    	ActionBar.Tab newTab1 = getSupportActionBar().newTab();
    	newTab1.setText("Tab 1 title");
    	ActionBar.Tab newTab2 = getSupportActionBar().newTab();
    	newTab2.setText("Tab 2 title");
    	
    	newTab0.setTabListener(this);
    	newTab1.setTabListener(this);
    	newTab2.setTabListener(this);

    	getSupportActionBar().addTab(newTab0);
    	getSupportActionBar().addTab(newTab1);
    	getSupportActionBar().addTab(newTab2);
    	
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		toastText("tab " + String.valueOf(tab.getPosition()) + " clicked");
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	private void toastText(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

}
