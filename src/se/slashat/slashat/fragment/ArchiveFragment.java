package se.slashat.slashat.fragment;

import se.slashat.slashat.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 * This class is just a wrapper fragment using different archive_fragment layouts depending on the device layout.
 * Bigger screens gets a layout with both list and details in the same layout while smaller devices only gets the list
 * and then the details are switched on top of it.
 */
public class ArchiveFragment extends Fragment{
	
	private static View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
	            parent.removeView(view);
		}
		
		try{
			view = inflater.inflate(R.layout.fragment_archive, container, false);
		} catch (InflateException e) {
	    }
		
		// Inflate the layout for this fragment
		return view;
	}
}
