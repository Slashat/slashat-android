package se.slashat.slashat.fragment;

import android.support.v4.app.Fragment;

public interface FragmentSwitcher {
	public void switchFragment(Fragment fragment,boolean addToBackstack);
}
