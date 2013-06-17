package se.slashat.slashat.fragment;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;

import se.slashat.slashat.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * This class is just a wrapper fragment using different fragment:about layouts depending on the device layout.
 * Bigger screens gets a layout with both list and details in the same layout while smaller devices only gets the list
 * and then the details are switched on top of it.
 */
public class AboutFragment extends SherlockListFragment {
	/**
	 * 
	 * @author Nicklas Löf
	 * 
	 */
	private static View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try {
			view = inflater.inflate(R.layout.fragment_about, container, false);
		} catch (InflateException e) {
		}

		setHasOptionsMenu(true);
		// Inflate the layout for this fragment
		return view;
	}
	
	//@Override
	//public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		//menu.clear();
	//}

}
