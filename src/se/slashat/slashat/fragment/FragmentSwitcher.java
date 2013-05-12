package se.slashat.slashat.fragment;

import java.io.Serializable;

import se.slashat.slashat.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentSwitcher implements Serializable {

	private static final long serialVersionUID = 1L;
	private FragmentManager fragmentManager;
	private static FragmentSwitcher instance;
	public static FragmentSwitcher getInstance() {
		return instance;
	}

	/**
	 * Start a new instance of the FragmentSwitcher with the specified
	 * Fragmentmanager
	 * 
	 * @param fragmentManager
	 */
	public static void initalize(FragmentManager fragmentManager) {
		instance = new FragmentSwitcher();
		instance.fragmentManager = fragmentManager;
	}

	/**
	 * Switch to a new fragment. If addToBackstack is true it's possible to go
	 * back to the previous fragment with the backbutton.
	 * 
	 * @param fragment
	 * @param addToBackstack
	 */
	public void switchFragment(Fragment fragment, boolean addToBackstack) {
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.add(R.id.fragment_container, fragment);
		if (addToBackstack){
			beginTransaction.setCustomAnimations(R.anim.slider_in,R.anim.noanimation,R.anim.noanimation,R.anim.slider_out);
		}else{
			beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}
		beginTransaction.show(fragment);
		
		if (addToBackstack) {
			beginTransaction.addToBackStack(null);
		} else {
			fragmentManager.popBackStack();
		}

		beginTransaction.commit();
	}

}
