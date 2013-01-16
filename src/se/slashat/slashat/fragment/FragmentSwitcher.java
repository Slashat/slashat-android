package se.slashat.slashat.fragment;

import java.io.Serializable;

import android.support.v4.app.Fragment;

public interface FragmentSwitcher extends Serializable{
	public void switchFragment(Fragment fragment,boolean addToBackstack);
}
