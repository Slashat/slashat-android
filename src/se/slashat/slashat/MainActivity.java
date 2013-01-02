package se.slashat.slashat;

import com.actionbarsherlock.app.SherlockActivity;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends SherlockActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//decide if we're showing live (tuesdays 19.00 -> 22.00) or Nextshow 
		
		//live for now
        try {
			Intent intent = new Intent(this, LiveActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
